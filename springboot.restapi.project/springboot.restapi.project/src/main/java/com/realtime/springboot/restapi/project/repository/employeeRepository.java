package com.realtime.springboot.restapi.project.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.realtime.springboot.restapi.project.entity.employeeEntity;

public interface employeeRepository extends MongoRepository<employeeEntity, String> {

	public employeeEntity findByemployeeId(Object String);

}
