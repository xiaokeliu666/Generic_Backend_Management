package com.xliu.qch.baseadmin.common.service;

import com.xliu.qch.baseadmin.common.pojo.PageCondition;
import com.xliu.qch.baseadmin.common.pojo.PageInfo;
import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.common.repository.CommonRepository;
import com.xliu.qch.baseadmin.util.CopyUtil;
import com.xliu.qch.baseadmin.util.ErrorUtil;
import com.xliu.qch.baseadmin.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Common ServiceImpl
 *
 * @param <V> Entity Class vo
 * @param <E> Entity Class
 * @param <T> id primary key type
 */
@Slf4j
public class CommonServiceImpl<V, E, T> implements CommonService<V, E, T> {

    private Class<V> entityVoClass;// Entity Class Vo

    private Class<E> entityClass;// Entity Class

    @Autowired
    private CommonRepository<E, T> commonRepository;// Inject entity repository

    public CommonServiceImpl() {
        Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        this.entityVoClass = (Class<V>) types[0];
        this.entityClass = (Class<E>) types[1];
    }

    @Override
    public Result<PageInfo<V>> page(V entityVo) {
        // Entity Class short of Pagination Information
        if (!(entityVo instanceof PageCondition)) {
            throw new RuntimeException("Entity Class" + entityVoClass.getName() + "didn't extend PageCondition.");
        }
        PageCondition pageCondition = (PageCondition) entityVo;
        Page<E> page = commonRepository.findAll(Example.of(CopyUtil.copy(entityVo, entityClass)), pageCondition.getPageable());
        return Result.of(PageInfo.of(page, entityVoClass));
    }

    @Override
    public Result<List<V>> list(V entityVo) {
        List<E> entityList = commonRepository.findAll(Example.of(CopyUtil.copy(entityVo, entityClass)));
        List<V> entityModelList = CopyUtil.copyList(entityList, entityVoClass);
        return Result.of(entityModelList);
    }

    @Override
    public Result<V> get(T id) {
        Optional<E> optionalE = commonRepository.findById(id);
        if (!optionalE.isPresent()) {
            return Result.of(null,false,"ID not existed！");
        }
        return Result.of(CopyUtil.copy(optionalE.get(), entityVoClass));
    }

    @Override
    public Result<V> save(V entityVo) {
        // The object passed in (fields may be missing)
        E entity = CopyUtil.copy(entityVo, entityClass);

        // Final Object which will be saved
        E entityFull = entity;

        // Null fields, ignore properties.
        // Will be used by BeanUtils
        List<String> ignoreProperties = new ArrayList<String>();

        // Get the latest data
        try {
            // Added true, Updated false.
            // The field of Id should be the first attribute, because for loop starts from 0
            boolean isInsert = false;

            // Using reflect to get Class(field means the member variables of class)
            for (Field field : entity.getClass().getDeclaredFields()) {
                // get access
                field.setAccessible(true);
                // field name
                String fieldName = field.getName();
                // field value
                Object fieldValue = field.get(entity);

                // Find the Id(primary key)
                if (field.isAnnotationPresent(Id.class)) {
                    if(!StringUtils.isEmpty(fieldValue)){
                        // If Id primary key is not null, then update
                        Optional<E> one = commonRepository.findById((T) fieldValue);
                        if (one.isPresent()) {
                            entityFull = one.get();
                        }
                    }else{
                        // If Id primary key is null, then add
                        fieldValue = UUIDUtil.getUUID();
                        //set method, the first parameter is object
                        field.set(entity, fieldValue);
                        isInsert = true;
                    }
                }
                if(isInsert && "createTime".equals(fieldName) && StringUtils.isEmpty(fieldValue)){
                    //set method, the first parameter is object
                    field.set(entity, new Date());
                }
                if("updateTime".equals(fieldName) && StringUtils.isEmpty(fieldValue)){
                    //set method, the first parameter is object
                    field.set(entity, new Date());
                }

                // Find out the field whose value is null, then ignore it
                if(null == fieldValue || field.isAnnotationPresent(NotFound.class)){
                    ignoreProperties.add(fieldName);
                }
            }
            /*
                org.springframework.beans BeanUtils.copyProperties(A,B); A -> B
                org.apache.commons.beanutils; BeanUtils.copyProperties(A,B); B->A
             */
            BeanUtils.copyProperties(entity, entityFull, ignoreProperties.toArray(new String[0]));
        } catch (IllegalAccessException e) {
            log.error(ErrorUtil.errorInfoToString(e));
        }

        E e = commonRepository.save(entityFull);
        return Result.of(CopyUtil.copy(e, entityVoClass));
    }

    @Override
    public Result<T> delete(T id) {
        commonRepository.deleteById(id);
        return Result.of(id);
    }
}
