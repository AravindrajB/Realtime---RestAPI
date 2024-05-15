package com.realtime.springboot.restapi.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.amazonaws.util.StringUtils;
import com.realtime.springboot.restapi.project.dto.employeeDto;
import com.realtime.springboot.restapi.project.service.employeeService;
import com.realtime.springboot.restapi.project.utility.BaseClass;

@RestController
@RequestMapping("/realtime")
public class employeeController {

	@Autowired
	employeeService employeeservice;

	@PostMapping("/addEmployee")
	public employeeDto addEmployee(@RequestBody employeeDto employeedto) throws Exception {

		final Pattern email_pattern = BaseClass.pattern(BaseClass.email_regex);
		final Pattern password_pattern = BaseClass.pattern(BaseClass.password_regex);
		employeeDto resultDto = new employeeDto();
		String employeeMail = employeedto.getEmployeeMail();

		if (!StringUtils.isNullOrEmpty(employeeMail)) {

			if (!email_pattern.matcher(employeeMail).matches()) {
				resultDto.setStatus(1);
				resultDto.setMessage("Your EmailID - " + employeeMail + " is not valid");
				return resultDto;
			}
		}

		String employeePassword = employeedto.getEmployeePassword();
		if (!StringUtils.isNullOrEmpty(employeePassword)) {

			if (!employeePassword.matches(".*[0-9].*")) {
				resultDto.setStatus(1);
				resultDto.setMessage("Digit is required");
				return resultDto;
			}
			if (!employeePassword.matches(".*[a-z].*")) {
				resultDto.setStatus(1);
				resultDto.setMessage("Lowercase letter is required");
				return resultDto;
			}
			if (!employeePassword.matches(".*[A-Z].*")) {
				resultDto.setStatus(1);
				resultDto.setMessage("Uppercase letter is required");
				return resultDto;
			}
			if (!employeePassword.matches(".*[@#$%^&+=!].*")) {
				resultDto.setStatus(1);
				resultDto.setMessage("Special character is required");
				return resultDto;
			}
			if (employeePassword.matches(".*\\s.*")) {
				resultDto.setStatus(1);
				resultDto.setMessage("Whitespace is not allowed");
				return resultDto;
			}

			if (!password_pattern.matcher(employeePassword).matches()) {
				resultDto.setStatus(1);
				resultDto.setMessage("Your Password is not Stronger");
				return resultDto;
			}
		}

		String employeeRole = employeedto.getEmployeeRole();
		if (employeeRole.length() <= 4) {
			resultDto.setStatus(1);
			resultDto.setMessage("EmployeeRole Length must be Greaterthan 3");
			return resultDto;
		}

		employeeDto employee = employeeservice.addEmployee(employeedto);

		return employee;
	}

	@GetMapping("/getEmployees")
	public List<employeeDto> getEmployees() throws Exception {
		List<employeeDto> employees = employeeservice.getEmployees();

		return employees;
	}

	@GetMapping("/getEmployee/{employeeId}")
	public employeeDto getEmployee(@PathVariable String employeeId) throws Exception {
		employeeDto employee = employeeservice.getEmployee(employeeId);
		return employee;
	}

	@PutMapping("/updateEmployee/{employeeId}")
	public employeeDto updateEmployee(@RequestBody employeeDto employeedto, @PathVariable String employeeId)
			throws Exception {

		employeeDto resultDto = new employeeDto();
		// Validation
		final Pattern email_pattern = BaseClass.pattern(BaseClass.email_regex);
		final Pattern password_pattern = BaseClass.pattern(BaseClass.password_regex);

		if (!StringUtils.isNullOrEmpty(employeedto.getEmployeeMail())) {

			if (!email_pattern.matcher(employeedto.getEmployeeMail()).matches()) {
				resultDto.setStatus(1); // error
				resultDto.setMessage("Your EmailID - " + employeedto.getEmployeeMail() + "is not valid");
				return resultDto;
			}
		}

		String employeePassword = employeedto.getEmployeePassword();
		if (!StringUtils.isNullOrEmpty(employeePassword)) {

			if (!employeePassword.matches(".*[0-9].*")) {
				resultDto.setStatus(1);
				resultDto.setMessage("Digit is required");
				return resultDto;
			}
			if (!employeePassword.matches(".*[a-z].*")) {
				resultDto.setStatus(1);
				resultDto.setMessage("Lowercase letter is required");
				return resultDto;
			}
			if (!employeePassword.matches(".*[A-Z].*")) {
				resultDto.setStatus(1);
				resultDto.setMessage("Uppercase letter is required");
				return resultDto;
			}
			if (!employeePassword.matches(".*[@#$%^&+=!].*")) {
				resultDto.setStatus(1);
				resultDto.setMessage("Special character is required");
				return resultDto;
			}
			if (employeePassword.matches(".*\\s.*")) {
				resultDto.setStatus(1);
				resultDto.setMessage("Whitespace is not allowed");
				return resultDto;
			}

			if (!password_pattern.matcher(employeePassword).matches()) {
				resultDto.setStatus(1);
				resultDto.setMessage("Your Password is not Stronger");
				return resultDto;
			}
		}

		String employeeRole = employeedto.getEmployeeRole();
		if (employeeRole.length() <= 4) {
			resultDto.setStatus(1);
			resultDto.setMessage("EmployeeRole Length must be Greaterthan 3");
			return resultDto;
		}

		resultDto = employeeservice.updateEmployee(employeedto, employeeId);
		return resultDto;
	}

	@DeleteMapping("/removeEmployee/{employeeId}")
	public employeeDto removeEmployee(@PathVariable String employeeId) throws Exception {

		employeeDto resultDto = employeeservice.removeEmployee(employeeId);

		return resultDto;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handlevalidationException(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<String, String>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldname = ((FieldError) error).getField();
			String errormsg = error.getDefaultMessage();
			errors.put(fieldname, errormsg);
		});

		return errors;
	}
}
