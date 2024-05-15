package com.realtime.springboot.restapi.project.serviceImp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.util.StringUtils;
import com.realtime.springboot.restapi.project.dto.employeeDto;
import com.realtime.springboot.restapi.project.entity.employeeEntity;
import com.realtime.springboot.restapi.project.repository.employeeRepository;
import com.realtime.springboot.restapi.project.service.employeeService;
import com.realtime.springboot.restapi.project.utility.BaseClass;

@Service
public class employeeServiceImpl implements employeeService {

	@Autowired
	employeeRepository employeerepo;

	@Override
	public employeeDto addEmployee(employeeDto employeedto) throws Exception {

		employeeEntity resultEntity = new employeeEntity();

		employeeDto resultDto = new employeeDto();

		try {

			Optional<employeeEntity> byId = employeerepo.findById(employeedto.getEmployeeId());

			if (!byId.isPresent()) {

				if (!StringUtils.isNullOrEmpty(employeedto.getEmployeeMail())
						&& !StringUtils.isNullOrEmpty(employeedto.getEmployeePassword())
						&& !StringUtils.isNullOrEmpty(employeedto.getEmployeeId())) {

					String currentMail = employeedto.getEmployeeMail();
					String encodedMail = BaseClass.encoder(currentMail);
					resultEntity.setEmployeeMail(encodedMail);
					String currentPassword = employeedto.getEmployeePassword();
					String encodedPassword = BaseClass.encoder(currentPassword);
					resultEntity.setEmployeePassword(encodedPassword);
					resultEntity.setEmployeeRole(employeedto.getEmployeeRole());

					String employeeId = employeedto.getEmployeeId();
					if (employeeId.matches("^TC-\\d{3}$")) {

						resultEntity.setEmployeeId(employeedto.getEmployeeId());
						resultEntity.setEmployeeName(employeedto.getEmployeeName());
						resultEntity.setEmployeeSalary(employeedto.getEmployeeSalary());
						resultEntity.setEmployeeExperience(employeedto.getEmployeeExperience());
						resultEntity.setCreatedDateTime(LocalDateTime.now());
						resultEntity.setUpdatedDateTime(LocalDateTime.now());
						employeerepo.save(resultEntity);
						resultDto.setStatus(0);
						resultDto.setMessage("Employee(" + employeedto.getEmployeeId() + ") - Saved Suceessfully");

					} else {

						resultDto.setStatus(1);
						resultDto.setMessage("The employee ID should be in this format: TC-000");
					}

				} else {

					resultDto.setStatus(1);
					resultDto.setMessage("Id, Mail & Password these are mandatory!");

				}

			} else {

				resultDto.setStatus(1); // has error
				resultDto.setMessage("Employee(" + employeedto.getEmployeeId() + ") - is Already Exists");
			}

		} catch (NullPointerException e) {

			resultDto.setStatus(1); // Null exception
			resultDto.setMessage("Id, Mail & Password are should be not null");
		}

		catch (Exception e) {

			resultDto.setStatus(1); // error
			resultDto.setMessage(e.getMessage());
		}

		return resultDto;
	}

	@Override
	public List<employeeDto> getEmployees() throws Exception {

		List<employeeEntity> allemployee = employeerepo.findAll();
		List<employeeDto> resultDto = new ArrayList<employeeDto>();
		for (employeeEntity employee : allemployee) {

			resultDto.add(this.getEmployeeDto(employee));
		}

		return resultDto;
	}

	private employeeDto getEmployeeDto(employeeEntity employeeEntity) {

		employeeDto employeedto = new employeeDto();
		employeedto.setEmployeeId(employeeEntity.getEmployeeId());
		employeedto.setEmployeeName(employeeEntity.getEmployeeName());
		employeedto.setEmployeeRole(employeeEntity.getEmployeeRole());
		employeedto.setEmployeeSalary(employeeEntity.getEmployeeSalary());
		employeedto.setCreatedDateTime(employeeEntity.getCreatedDateTime());
		employeedto.setUpdatedDateTime(employeeEntity.getUpdatedDateTime());
		employeedto.setEmployeeExperience(employeeEntity.getEmployeeExperience());
		employeedto.setEmployeeMail(BaseClass.decoder(employeeEntity.getEmployeeMail()));
		employeedto.setEmployeePassword(BaseClass.decoder(employeeEntity.getEmployeePassword()));

		return employeedto;
	}

	@Override
	public employeeDto getEmployee(final String employeeId) throws Exception {

//		employeeEntity byemployeeId = employeerepo.findByemployeeId(employeeId);
		employeeDto resultDto = new employeeDto();
		try {

			Optional<employeeEntity> byId = employeerepo.findById(employeeId);

			if (byId.isPresent()) {

				employeeEntity employeeEntity = byId.get();
				resultDto.setEmployeeId(employeeEntity.getEmployeeId());
				resultDto.setEmployeeName(employeeEntity.getEmployeeName());
				resultDto.setEmployeeRole(employeeEntity.getEmployeeRole());
				resultDto.setEmployeeSalary(employeeEntity.getEmployeeSalary());
				resultDto.setCreatedDateTime(employeeEntity.getCreatedDateTime());
				resultDto.setUpdatedDateTime(employeeEntity.getUpdatedDateTime());
				resultDto.setEmployeeExperience(employeeEntity.getEmployeeExperience());
				resultDto.setEmployeeMail(BaseClass.decoder(employeeEntity.getEmployeeMail()));
				resultDto.setEmployeePassword(BaseClass.decoder(employeeEntity.getEmployeePassword()));

			} else {

				resultDto.setStatus(1);
				resultDto.setMessage("Employee(" + employeeId + ") - is Not Found");
			}

		} catch (Exception e) {
			resultDto.setStatus(1);
			resultDto.setMessage(e.getMessage());
		}

		return resultDto;
	}

	@Override
	public employeeDto updateEmployee(employeeDto employeedto, String employeeId) throws Exception {

//		employeeEntity resultEntity = new employeeEntity();
		employeeDto resultDto = new employeeDto();

		try {
			Optional<employeeEntity> byId = employeerepo.findById(employeeId);

			if (byId.isPresent()) {

				employeeEntity resultEntity = byId.get();

				if (!StringUtils.isNullOrEmpty(employeedto.getEmployeeId())) {

					if (!resultEntity.getEmployeeId().equalsIgnoreCase(employeeId)
							|| !resultEntity.getEmployeeId().equalsIgnoreCase(employeedto.getEmployeeId())) {
						resultDto.setStatus(1);
						resultDto.setMessage("Employee ID cannot be changed, so please give the same ID!");
						return resultDto;
					}
				}

				if (!StringUtils.isNullOrEmpty(employeedto.getEmployeeMail())) {

					resultEntity.setEmployeeMail(BaseClass.encoder(employeedto.getEmployeeMail()));
				}
				if (!StringUtils.isNullOrEmpty(employeedto.getEmployeePassword())) {

					resultEntity.setEmployeePassword(BaseClass.encoder(employeedto.getEmployeePassword()));
				}
				if (!StringUtils.isNullOrEmpty(employeedto.getEmployeeRole())) {

					resultEntity.setEmployeeRole(employeedto.getEmployeeRole());
				}

				if (!StringUtils.isNullOrEmpty(employeedto.getEmployeeName())) {

					resultEntity.setEmployeeName(employeedto.getEmployeeName());
				}

				resultEntity.setEmployeeExperience(
						employeedto.getEmployeeExperience() == 0 ? resultEntity.getEmployeeExperience()
								: employeedto.getEmployeeExperience());

				resultEntity.setEmployeeSalary(employeedto.getEmployeeSalary() == 0 ? resultEntity.getEmployeeSalary()
						: employeedto.getEmployeeSalary());

				resultEntity
						.setCreatedDateTime(employeedto.getCreatedDateTime() == null ? resultEntity.getCreatedDateTime()
								: employeedto.getCreatedDateTime());

				resultEntity.setUpdatedDateTime(LocalDateTime.now());
				resultEntity.setEmployeeId(employeeId);
				employeerepo.save(resultEntity);
				resultDto.setStatus(0);
				resultDto.setMessage("Employee { " + employeeId + " } Updated Successfully");

			} else {

				resultDto.setStatus(1);
				resultDto.setMessage("Employee { " + employeeId + " } - is Not Found");

			}
		} catch (Exception e) {

			resultDto.setStatus(1);
			resultDto.setMessage(e.getMessage());
		}

		return resultDto;
	}

	@Override
	public employeeDto removeEmployee(String employeeId) throws Exception {

		employeeDto resultDto = new employeeDto();

		try {

			Optional<employeeEntity> byId = employeerepo.findById(employeeId);

			if (byId.isPresent()) {

				employeerepo.deleteById(employeeId);
				resultDto.setStatus(0);
				resultDto.setMessage("This " + employeeId + " is Deleted Successfully!");

			} else {

				resultDto.setStatus(1);
				resultDto.setMessage("This " + employeeId + " is not Found...");
			}
		} catch (Exception e) {

			resultDto.setStatus(1);
			resultDto.setMessage(e.getMessage());
		}

		return resultDto;
	}
}
