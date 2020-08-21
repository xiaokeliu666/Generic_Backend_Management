package com.xliu.qch.baseadmin.util;

import com.xliu.qch.baseadmin.annotation.Between;
import com.xliu.qch.baseadmin.annotation.In;
import com.xliu.qch.baseadmin.annotation.Like;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Transient;
import org.springframework.util.StringUtils;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Util for the SQL Concat
 */
@Slf4j
public class SqlUtil {

    /**
     * Used to determine the type of database
     * MySQL：com.mysql.jdbc.Driver
     * Oracle：oracle.jdbc.OracleDriver
     */
    @Value("${string.datasource.driver-class-name}")
    private static String sqlType;

    /**
     * Automatically append SQL 'and' condition, support customized annotation: @Like @Between @In
     *
     * @param entity           Entity Object
     * @param sql              SQL before appending
     * @param ignoreProperties ignoreProperties
     */
    public static void appendQueryColumns(Object entity, StringBuilder sql, String... ignoreProperties) {

        try {
            // Ignored properties
            List<String> ignoreList1 = Arrays.asList(ignoreProperties);
            // Ignore pagination field by default
            List<String> ignoreList2 = Arrays.asList("class", "pageable", "page", "rows", "sidx", "sord");

            // Get Class by using reflect
            for (Field field : entity.getClass().getDeclaredFields()) {
                // get the access
                field.setAccessible(true);
                // filed name
                String fieldName = field.getName();
                // filed value
                Object fieldValue = field.get(entity);
                // Annotation @Transient means this field is not to be serialized so it will not be persisted in the database
                // If a field is annotated by @Transient, we should ignore it
                if (!field.isAnnotationPresent(Transient.class)) {
                    String column = new PropertyNamingStrategy.SnakeCaseStrategy().translate(fieldName).toLowerCase();
                    // Field value is null?
                    if (!StringUtils.isEmpty(fieldValue)) {
                        // Mapper relation: object field(CamelCase) -> database row(underscore)
                        if (!ignoreList1.contains(fieldName) && !ignoreList2.contains(fieldName)) {
                            // Fuzzy Query
                            if (field.isAnnotationPresent(Like.class)) {
                                sql.append(" and " + column + " like '%" + fieldValue + "%'");
                            }
                            //Equivalent query
                            else {
                                sql.append(" and " + column + " = '" + fieldValue + "'");
                            }
                        }
                    } else {
                        // Interval query
                        if (field.isAnnotationPresent(Between.class)) {
                            // Get the min value
                            Field minField = entity.getClass().getDeclaredField(field.getAnnotation(Between.class).min());
                            minField.setAccessible(true);
                            Object minVal = minField.get(entity);
                            // Get the max value
                            Field maxField = entity.getClass().getDeclaredField(field.getAnnotation(Between.class).max());
                            maxField.setAccessible(true);
                            Object maxVal = maxField.get(entity);
                            // Interval query
                            if (field.getType().getName().equals("java.util.Date")) {
                                //MySQL
                                if(sqlType.toLowerCase().contains("mysql")){

                                }
                                //Oracle
                                if(sqlType.toLowerCase().contains("oracle")){
                                    if (!StringUtils.isEmpty(minVal)) {
                                        sql.append(" and " + column + " > to_date( '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) minVal) + "','yyyy-mm-dd hh24:mi:ss')");
                                    }
                                    if (!StringUtils.isEmpty(maxVal)) {
                                        sql.append(" and " + column + " < to_date( '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) maxVal) + "','yyyy-mm-dd hh24:mi:ss')");
                                    }
                                }
                            }
                        }

                        // In Query
                        if (field.isAnnotationPresent(In.class)) {
                            // Get the 'In' value
                            Field values = entity.getClass().getDeclaredField(field.getAnnotation(In.class).values());
                            values.setAccessible(true);
                            List<String> valuesList = (List<String>) values.get(entity);
                            if (valuesList != null && valuesList.size() > 0) {
                                String inValues = "";
                                for (String value : valuesList) {
                                    inValues = inValues + "'" + value + "'";
                                }
                                sql.append(" and " + column + " in (" + inValues + ")");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(ErrorUtil.errorInfoToString(e));
        }
    }

    /**
     *
     * @param entity Append entity object field automatically
     * @param ignoreProperties Dynamical field
     * @return sql
     */
    public static StringBuilder appendFields(Object entity, String... ignoreProperties) {
        StringBuilder sql = new StringBuilder();
        List<String> ignoreList = Arrays.asList(ignoreProperties);
        try {
            sql.append("select ");

            for (Field field : entity.getClass().getDeclaredFields()) {
                // get access
                field.setAccessible(true);
                String fieldName = field.getName();// field name
                Object fieldValue = field.get(entity);// field value
                // skip if @transient or ignored field
                if (!field.isAnnotationPresent(Transient.class) && !ignoreList.contains(fieldName)) {
                    // Append query field, convert CamelCase to underscore
                    sql.append(new PropertyNamingStrategy.SnakeCaseStrategy().translate(fieldName).toLowerCase()).append(" ").append(",");
                }
            }
            // delete the comma
            sql.deleteCharAt(sql.length() - 1);

            String tableName = entity.getClass().getAnnotation(Table.class).name();
            sql.append("from ").append(tableName).append(" where '1' = '1' ");
        } catch (IllegalAccessException e) {
            log.error(ErrorUtil.errorInfoToString(e));
        }
        return sql;
    }

    /**
     * sql Escape
     * Dynamically append SQL, using escape to avoid SQL Injection
     */
    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char src = str.charAt(i);
            switch (src) {
                case '\'':
                    sb.append("''");
                    break;
                case '\"':
                case '\\':
                    sb.append('\\');
                default:
                    sb.append(src);
                    break;
            }
        }
        return sb.toString();
    }
}
