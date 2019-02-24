package com.test.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.test.entity.Person;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDao {

    //通过 @PersistenceContext 获取到和当前事务关联的 EntityManager 对象
    @PersistenceContext
    EntityManager entityManager;

    public void save(Person person){
        entityManager.persist(person);
    }

}
