package com.test;

import com.test.entity.Person;
import com.test.service.PersonService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Tests {

    private ConfigurableApplicationContext ctx;
    private PersonService personService;

    @Before
    public void before() {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        personService = ctx.getBean(PersonService.class);
    }

    @After
    public void after() {
        ctx.close();
    }

    @Test
    public void testPersonService() {
        Person p1 = new Person();
        p1.setLastName("aa");
        p1.setEmail("aa@163.com");
        p1.setAge(11);

        Person p2 = new Person();
        p2.setLastName("bb");
        p2.setEmail("bb@163.com");
        p2.setAge(22);

        System.out.println(personService.getClass().getName());
        personService.savePersons(p1, p2);
    }

}
