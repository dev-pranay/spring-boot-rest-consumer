package com.pk.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pk.main.model.Employee;
import com.pk.main.service.EmployeeService;

/**
 * @author PranaySK
 */

@RestController
@RequestMapping("/")
public class EmployeeController {

	@Autowired
	EmployeeService service;

	@GetMapping(path = "employee")
	public List<Employee> callGetEmployees() {
		return service.callGetEmployees();
	}

	@GetMapping(path = "employee/{id}")
	public Employee callGetEmployee(@PathVariable("id") Integer id) {
		return service.callGetEmployeeById(id);
	}

	@PostMapping(path = "employee")
	public Employee callAddEmployee(@RequestBody Employee employee) {
		return service.callAddEmployee(employee);
	}

	@PutMapping(path = "employee/{id}")
	public String callUpdateEmployee(@PathVariable("id") Integer id, @RequestBody Employee employee) {
		return service.callUpdateEmployee(id, employee);
	}

	@DeleteMapping(path = "employee/{id}")
	public String callDeleteEmployee(@PathVariable("id") Integer id) {
		return service.callDeleteEmployee(id);
	}

}
