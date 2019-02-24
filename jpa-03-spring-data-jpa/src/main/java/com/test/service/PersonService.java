package com.test.service;

import java.util.List;

import com.test.dao.PersonRepository;
import com.test.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;
	
	@Transactional
	public void savePersons(List<Person> personList){
		personRepository.save(personList);
	}
	
	@Transactional
	public void updatePersonEmail(Integer id, String email){
		personRepository.updatePersonEmail(id, email);
	}

}
