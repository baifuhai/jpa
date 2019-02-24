package com.test.commonrepositorymethod;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public class CommonMethodRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CommonMethodRepository<T, ID> {

	public CommonMethodRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
	}

	@Override
	public void commonRepositoryMethod() {
		System.out.println("commonRepositoryMethod...");
	}

}
