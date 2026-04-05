package com.gallelloit.employees.service;

import com.gallelloit.employees.entity.Department;
import com.gallelloit.employees.entity.Employee;
import com.gallelloit.employees.repository.DepartmentRepository;
import com.gallelloit.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
class EmployeeIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("employees")
                    .withUsername("user")
                    .withPassword("password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void shouldSaveAndRetrieveEmployee() {
        Department dept = Department.builder()
                .name("IT")
                .build();

        dept = departmentRepository.save(dept);

        Employee emp = Employee.builder()
                .name("Pablo")
                .email("pablo@test.com")
                .department(dept)
                .build();

        employeeRepository.save(emp);

        List<Employee> employees = employeeRepository.findAll();

        assertEquals(1, employees.size());
        assertEquals("Pablo", employees.get(0).getName());
    }
}