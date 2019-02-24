package com.test.dao;

import com.test.entity.Person;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 为某一个 Repository 上添加自定义方法
 * 为所有的 Repository 都添加自定义方法（用的不多）
 */
public class PersonRepositoryImpl implements PersonDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void customRepositoryMethod() {
        Person person = entityManager.find(Person.class, 1);
        System.out.println(person);
    }

}
