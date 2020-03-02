package com.pk.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.pk.main.controller.EmployeeController;
import com.pk.main.model.Employee;
import com.pk.main.service.EmployeeService;

/**
 * @author PranaySK
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringRestConsumerApplication.class)
@WebMvcTest(value = EmployeeController.class)
class SpringRestConsumerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService employeeService;

	@Test
	public void contextLoads() {
		assertTrue(true);
	}

	@Test
	public void testRetrieveAllEmployees() throws Exception {
		Employee e0 = new Employee(1, "Emp1", "Java");
		Employee e1 = new Employee(2, "Emp2", "SQL");
		Employee e2 = new Employee(3, "Emp3", "JS");

		List<Employee> mockEmployeeList = new ArrayList<>();
		mockEmployeeList.add(e0);
		mockEmployeeList.add(e1);
		mockEmployeeList.add(e2);

		// employeeService.getEmployees to respond back with mockEmployee list
		Mockito.when(employeeService.callGetEmployees()).thenReturn(mockEmployeeList);
		// Send request to /employee
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{id:1,name:Emp1,tech:Java},{id:2,name:Emp2,tech:SQL},{id:3,name:Emp3,tech:JS}]";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void testRetrieveEmployeeById() throws Exception {
		Employee mockEmployee = new Employee(3, "Emp3", "JS");

		// employeeService.getEmployeeById to respond back with mockEmployee
		Mockito.when(employeeService.callGetEmployeeById(Mockito.anyInt())).thenReturn(mockEmployee);
		// Send request to /employee by passing employee Id as path variable
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee/3").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{id:3,name:Emp3,tech:JS}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void testCreateEmployee() throws Exception {
		Employee mockEmployee = new Employee(5, "Emp5", "Java");
		String mockEmployeeJson = "{\"id\":5,\"name\":\"Emp5\",\"tech\":\"Java\"}";

		// employeeService.addEmployee to respond back with mockEmployee
		Mockito.when(employeeService.callAddEmployee(Mockito.any(Employee.class))).thenReturn(mockEmployee);

		// Send employee to be newly added as body to /employee
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employee").accept(MediaType.APPLICATION_JSON)
				.content(mockEmployeeJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	public void testUpdateEmployee() throws Exception {
		String mockResStr = "Employee with Id - { 2 } is updated";
		String mockEmployeeJson = "{\"id\":2,\"name\":\"Emp2\",\"tech\":\"SQL\"}";

		// employeeService.updateEmployee to respond back with mockEmployee
		Mockito.when(employeeService.callUpdateEmployee(Mockito.anyInt(), Mockito.any(Employee.class)))
				.thenReturn(mockResStr);
		// Send employee to be updated as body to /employee
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/2").accept(MediaType.APPLICATION_JSON)
				.content(mockEmployeeJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	public void testDeleteEmployee() throws Exception {
		String mockResStr = "Employee with Id - { 3 } is deleted";
		// employeeController.deleteEmployeeById to respond back with String message
		Mockito.when(employeeService.callDeleteEmployee(Mockito.anyInt())).thenReturn(mockResStr);
		verify(employeeService, never()).callGetEmployeeById(Mockito.anyInt());
		// Send request to /employee by passing Id as path variable
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/employee/3").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

}
