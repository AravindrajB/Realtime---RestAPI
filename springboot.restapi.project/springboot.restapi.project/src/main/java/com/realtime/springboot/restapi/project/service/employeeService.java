package com.realtime.springboot.restapi.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.realtime.springboot.restapi.project.dto.employeeDto;

@Service
public interface employeeService {

	public employeeDto addEmployee(employeeDto employeedto)throws Exception;

	public List<employeeDto> getEmployees() throws Exception;

	public employeeDto getEmployee(String employeeId)throws Exception;

	public employeeDto updateEmployee(employeeDto employeedto, String employeeId) throws Exception;

	public employeeDto removeEmployee(String employeeId)throws Exception;

}
