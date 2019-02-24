package com.test.commonrepositorymethod;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonMethodRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

	void commonRepositoryMethod();
	
}
