package com.pk.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.pk.main.model.Employee;

/**
 * @author PranaySK
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringRestConsumerApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationIntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		assertTrue(true);
	}

	@Test
	void testRetrieveAllEmployees() {
		Employee[] employees = this.restTemplate.getForObject("/employee", Employee[].class);

		assertEquals(1, employees[0].getId());
		assertEquals("Emp2", employees[1].getName());
		assertEquals("JS", employees[2].getTech());
	}

	@Test
	void testRetrieveEmployeeById() {
		Employee employee = this.restTemplate.getForObject("/employee/2", Employee.class);

		assertEquals(2, employee.getId());
		assertEquals("Emp2", employee.getName());
		assertEquals("SQL", employee.getTech());
	}

	@Test
	void testAddEmployee() {
		Employee employee = new Employee(6, "Emp6", "Java");

		Employee newEmployee = this.restTemplate.postForObject("/employee", employee, Employee.class);

		assertEquals(6, newEmployee.getId());
		assertEquals("Emp6", newEmployee.getName());
		assertEquals("Java", newEmployee.getTech());
	}

	@Test
	void testUpdateEmployee() {
		int id = 5;
		Employee employee = new Employee(id, "Emp7", "SQL");

		this.restTemplate.put("/employee/" + id, employee);

		Employee updatedEmployee = restTemplate.getForObject("/employee/" + id, Employee.class);
		assertNotNull(updatedEmployee);
		assertEquals("Emp7", updatedEmployee.getName());
	}

	@Test
	void testDeleteEmployee() {
		int id = 4;
		Employee employee = restTemplate.getForObject("/employee/" + id, Employee.class);
		assertNotNull(employee);

		this.restTemplate.delete("/employee/" + id);

		try {
			employee = restTemplate.getForObject("/employee/" + id, Employee.class);
		} catch (final Exception ex) {
			assertEquals("Requested employee does not exist", ex.getMessage());
		}
	}

}
