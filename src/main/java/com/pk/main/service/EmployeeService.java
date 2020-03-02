package com.pk.main.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pk.main.constants.ErrorConstants;
import com.pk.main.exceptions.DataInsertionException;
import com.pk.main.exceptions.DbException;
import com.pk.main.model.Employee;

/**
 * @author PranaySK
 */

@Service
public class EmployeeService {

	private static Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	RestTemplate restTemplate;

	@Value("${api.url}")
	String apiUrl;

	public List<Employee> callGetEmployees() {
		try {
			Employee[] employee = restTemplate.getForObject(apiUrl, Employee[].class);
			logger.debug("Employee list -> {}", Arrays.asList(employee));
			return Arrays.asList(employee);
		} catch (Exception e) {
			throw new DbException(ErrorConstants.DATA_RET_ERROR, "Error while retrieving employees using API");
		}
	}

	public Employee callGetEmployeeById(Integer id) {
		Map<String, Integer> params = new HashMap<>();
		params.put("id", id);
		Employee employee = restTemplate.getForObject(apiUrl + "{id}", Employee.class, params);
		logger.debug("Employee -> {}", employee);
		if (employee == null) {
			throw new DbException(ErrorConstants.DATA_NOT_FOUND_ERROR, "Requested employee does not exist");
		}
		return employee;
	}

	public Employee callAddEmployee(Employee employee) {
		try {
			Employee newEmployee = restTemplate.postForObject(apiUrl, employee, Employee.class);
			logger.debug("Added employee -> {}", newEmployee);
			return newEmployee;
		} catch (Exception e) {
			throw new DataInsertionException(ErrorConstants.INSERTION_ERROR, "Error while adding employee using API");
		}
	}

	public String callUpdateEmployee(Integer id, Employee employee) {
		try {
			Map<String, Integer> params = new HashMap<>();
			params.put("id", id);
			restTemplate.put(apiUrl + "{id}", employee, params);
			logger.debug("Updated employee -> {}", id);
			return "Employee with Id - { " + id + " } is updated";
		} catch (Exception e) {
			throw new DbException(ErrorConstants.DATA_NOT_FOUND_ERROR, "Employee to be updated not found");
		}
	}

	public String callDeleteEmployee(Integer id) {
		try {
			this.callGetEmployeeById(id);
			Map<String, Integer> params = new HashMap<>();
			params.put("id", id);
			restTemplate.delete(apiUrl + "{id}", params);
			logger.debug("Deleted employee -> {}", id);
			return "Employee with Id - { " + id + " } is deleted";
		} catch (Exception e) {
			throw new DbException(ErrorConstants.DATA_DELETION_ERROR, "Error while deleting employee using API");
		}
	}

}
