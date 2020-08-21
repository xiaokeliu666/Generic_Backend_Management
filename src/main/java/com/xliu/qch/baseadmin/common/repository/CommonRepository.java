package com.xliu.qch.baseadmin.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Generic Repository
 *
 * @param <E> Entity
 * @param <T> Primary key type
 */
@NoRepositoryBean
public interface CommonRepository<E,T> extends JpaRepository<E,T>, JpaSpecificationExecutor<E> {

}
