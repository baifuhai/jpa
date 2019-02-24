package com.test.service;

import com.test.dao.PersonDao;
import com.test.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

    @Autowired
    PersonDao personDao;

    @Transactional
    public void savePersons(Person p1, Person p2){
        personDao.save(p1);

        int i = 1 / 0;

        personDao.save(p2);
    }

}
