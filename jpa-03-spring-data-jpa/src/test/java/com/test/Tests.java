package com.test;

import com.test.commonrepositorymethod.UserRepository;
import com.test.dao.PersonRepository;
import com.test.entity.Person;
import com.test.service.PersonService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Tests {

    private ConfigurableApplicationContext ctx;
    private PersonRepository personRepository;
    private PersonService personService;

    @Before
    public void before() {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        personRepository = ctx.getBean(PersonRepository.class);
        personService = ctx.getBean(PersonService.class);
    }

    @After
    public void after() {
        ctx.close();
    }

    @Test
    public void test01() {
        System.out.println(personRepository.getClass().getName());
    }

    @Test
    public void testQuery01() {
        Person person = personRepository.getByLastName("aa");
        System.out.println(person);
    }

    @Test
    public void testQuery02() {
        List<Person> personList = personRepository.getByLastNameStartingWithAndIdLessThan("a", 10);
        for (Person person : personList) {
            System.out.println(person);
        }
    }

    @Test
    public void testQuery03() {
        List<Person> personList = personRepository.getByLastNameEndingWithAndIdLessThan("a", 10);
        for (Person person : personList) {
            System.out.println(person);
        }
    }

    @Test
    public void testQuery04() {
        List<Person> personList = personRepository.getByEmailInAndBirthLessThan(Arrays.asList("a@126.com", "b@126.com"), new Date());
        for (Person person : personList) {
            System.out.println(person);
        }
    }

    @Test
    public void testQuery05() {
        List<Person> personList = personRepository.getByAddress_IdGreaterThan(1);
        for (Person person : personList) {
            System.out.println(person);
        }
    }

    @Test
    public void testQueryAnnotation() {
        Person person = personRepository.getMaxIdPerson();
        System.out.println(person);
    }

    @Test
    public void testQueryAnnotationParam1() {
        List<Person> personList = personRepository.testQueryAnnotationParam1("a", "a@126.com");
        for (Person person : personList) {
            System.out.println(person);
        }
    }

    @Test
    public void testQueryAnnotationParam2() {
        List<Person> personList = personRepository.testQueryAnnotationParam2("a", "a@126.com");
        for (Person person : personList) {
            System.out.println(person);
        }
    }

    @Test
    public void testQueryAnnotationLikeParam1() {
		List<Person> personList = personRepository.testQueryAnnotationLikeParam1("a", "a@126.com");
        for (Person person : personList) {
            System.out.println(person);
        }
    }

    @Test
    public void testQueryAnnotationLikeParam2() {
        List<Person> personList = personRepository.testQueryAnnotationLikeParam2("a", "a@126.com");
        for (Person person : personList) {
            System.out.println(person);
        }
    }

    @Test
    public void testNativeQuery() {
        long count = personRepository.getTotalCount();
        System.out.println(count);
    }

    @Test
    public void testModifying() {
//		personRepository.updatePersonEmail(1, "c@126.com");
        personService.updatePersonEmail(1, "c@126.com");
    }

    @Test
    public void testCrudRepository() {
        List<Person> personList = new ArrayList<>();

        for(int i = 1; i <= 26; i++){
            Person person = new Person();
            person.setLastName("a" + i);
            person.setEmail("a" + i + "@126.com");
            person.setAge(i);
            person.setBirth(new Date());
            person.setAddressId(1);

            personList.add(person);
        }

        personService.savePersons(personList);
    }

    @Test
    public void testPagingAndSortingRepository() {
        //pageNo 从 0 开始
        int pageNo = 3 - 1;
        int pageSize = 5;

        //Sort 封装了排序的信息
        //Order 是具体针对于某一个属性进行升序还是降序
        Order order1 = new Order(Direction.DESC, "id");
        Order order2 = new Order(Direction.ASC, "email");
        Sort sort = new Sort(order1, order2);

        //Pageable 接口通常使用的其 PageRequest 实现类，其中封装了需要分页的信息排序相关的
        Pageable pageable = new PageRequest(pageNo, pageSize, sort);
        Page<Person> page = personRepository.findAll(pageable);

        System.out.println("当前第几页: " + (page.getNumber() + 1));
        System.out.println("每页记录数: " + page.getSize());
        System.out.println("总页数: " + page.getTotalPages());
        System.out.println("总记录数: " + page.getTotalElements());
        System.out.println("当前页面的记录数: " + page.getNumberOfElements());
        System.out.println("当前页面的数据: ");

        List<Person> list = page.getContent();
        for (Person person : list) {
            System.out.println(person);
        }
    }

    /**
     * 相当于 jpa 的 merge 方法
     *
     * 如果 person 没有 id，新增，true
     * 如果 person 有 id
     *      如果数据库中没有，新增，false
     *      如果数据库中有，person 属性复制过来，更新，false
     */
    @Test
    public void testJpaRepository() {
        Person person = new Person();
        person.setId(2);
        person.setLastName("xyz");
        person.setEmail("xyz@126.com");
        person.setAge(27);
        person.setBirth(new Date());

        Person person2 = personRepository.saveAndFlush(person);

        System.out.println(person);
        System.out.println(person2);
        System.out.println(person == person2);
    }

    /**
     * 实现带查询条件的分页
     * id > 3 的条件
     *
     * 调用 JpaSpecificationExecutor 的 Page<T> findAll(Specification<T> spec, Pageable pageable);
     *      Specification: 封装了 JPA Criteria 查询的查询条件
     *      Pageable: 封装了请求分页的信息：例如 pageNo, pageSize, Sort
     */
    @Test
    public void testJpaSpecificationExecutor() {
        //通常使用 Specification 的匿名内部类
        Specification<Person> specification = new Specification<Person>() {
            /**
             * @param root 代表查询的实体类
             * @param query 可以从中可到 Root 对象，即告知 JPA Criteria 查询要查询哪一个实体类
             *                  还可以来添加查询条件，还可以结合 EntityManager 对象得到最终查询的 TypedQuery 对象，用的不多
             * @param cb CriteriaBuilder 对象，用于创建 Criteria 相关对象的工厂，当然可以从中获取到 Predicate 对象
             * @return Predicate 类型，代表一个查询条件
             */
            @Override
            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path path = root.get("id");
                Predicate predicate = cb.gt(path, 3);
                return predicate;
            }
        };

        int pageNo = 3 - 1;
        int pageSize = 5;
        Pageable pageable = new PageRequest(pageNo, pageSize);

        Page<Person> page = personRepository.findAll(specification, pageable);

        System.out.println("当前第几页: " + (page.getNumber() + 1));
        System.out.println("每页记录数: " + page.getSize());
        System.out.println("总页数: " + page.getTotalPages());
        System.out.println("总记录数: " + page.getTotalElements());
        System.out.println("当前页面的记录数: " + page.getNumberOfElements());
        System.out.println("当前页面的数据: ");

        List<Person> list = page.getContent();
        for (Person person : list) {
            System.out.println(person);
        }
    }

    @Test
    public void testCustomRepositoryMethod() {
        personRepository.customRepositoryMethod();
    }

    @Test
    public void testCommonRepositoryMethod() {
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:com/test/commonrepositorymethod/applicationContext.xml");
        UserRepository userRepository = ctx.getBean(UserRepository.class);
        userRepository.commonRepositoryMethod();
    }

}
