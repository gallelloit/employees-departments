package com.gallelloit.employees.service;

import com.gallelloit.employees.dto.EmployeeCreateRequest;
import com.gallelloit.employees.dto.EmployeeDTO;
import com.gallelloit.employees.entity.Department;
import com.gallelloit.employees.entity.Employee;
import com.gallelloit.employees.repository.DepartmentRepository;
import com.gallelloit.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Department testDepartment;
    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testDepartment = Department.builder()
                .id(1L)
                .name("Engineering")
                .build();

        testEmployee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .department(testDepartment)
                .build();
    }

    @Test
    void findAll_shouldReturnAllEmployees() {
        List<Employee> employees = List.of(testEmployee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeDTO> result = employeeService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        EmployeeDTO employeeDTO = result.get(0);
        assertEquals(testEmployee.getId(), employeeDTO.id());
        assertEquals(testEmployee.getName(), employeeDTO.name());
        assertEquals(testEmployee.getEmail(), employeeDTO.email());
        assertEquals(testDepartment.getId(), employeeDTO.departmentId());
        assertEquals(testDepartment.getName(), employeeDTO.departmentName());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of());

        List<EmployeeDTO> result = employeeService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void create_shouldCreateEmployeeWhenDepartmentExists() {
        EmployeeCreateRequest request = new EmployeeCreateRequest(
                "Jane Smith",
                "jane.smith@example.com",
                1L
        );

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        EmployeeDTO result = employeeService.create(request);

        assertNotNull(result);
        assertEquals(testEmployee.getId(), result.id());
        assertEquals(testEmployee.getName(), result.name());
        assertEquals(testEmployee.getEmail(), result.email());
        assertEquals(testDepartment.getId(), result.departmentId());
        assertEquals(testDepartment.getName(), result.departmentName());

        verify(departmentRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void create_shouldThrowExceptionWhenDepartmentNotFound() {
        EmployeeCreateRequest request = new EmployeeCreateRequest(
                "Jane Smith",
                "jane.smith@example.com",
                999L
        );

        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeService.create(request);
        });

        assertEquals("Department not found", exception.getMessage());
        verify(departmentRepository, times(1)).findById(999L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void updateEmployee_shouldUpdateEmployeeWhenFound() {
        Employee updatedEmployee = Employee.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        Employee result = employeeService.updateEmployee(1L, updatedEmployee);

        assertNotNull(result);
        assertEquals("John Updated", testEmployee.getName());
        assertEquals("john.updated@example.com", testEmployee.getEmail());

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(testEmployee);
    }

    @Test
    void updateEmployee_shouldThrowExceptionWhenEmployeeNotFound() {
        Employee updatedEmployee = Employee.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .build();

        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeService.updateEmployee(999L, updatedEmployee);
        });

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, times(1)).findById(999L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_shouldDeleteEmployeeWhenExists() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEmployee_shouldHandleNonExistentEmployee() {
        doThrow(new RuntimeException("Employee not found")).when(employeeRepository).deleteById(999L);

        assertThrows(RuntimeException.class, () -> {
            employeeService.deleteEmployee(999L);
        });

        verify(employeeRepository, times(1)).deleteById(999L);
    }
}
