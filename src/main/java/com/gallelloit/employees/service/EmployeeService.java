package com.gallelloit.employees.service;

import com.gallelloit.employees.dto.EmployeeCreateRequest;
import com.gallelloit.employees.dto.EmployeeDTO;
import com.gallelloit.employees.entity.Department;
import com.gallelloit.employees.entity.Employee;
import com.gallelloit.employees.mapper.EmployeeMapper;
import com.gallelloit.employees.repository.DepartmentRepository;
import com.gallelloit.employees.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public List<EmployeeDTO> findAll() {

        return employeeRepository.findAll().stream().map(EmployeeMapper::toDTO).toList();
    }

    public EmployeeDTO create(EmployeeCreateRequest employeeRequest) {

        final Department department = departmentRepository.findById(employeeRequest.departmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        final Employee employee = Employee.builder()
                .name(employeeRequest.name())
                .email(employeeRequest.email())
                .department(department)
                .build();

        return EmployeeMapper.toDTO(employeeRepository.save(employee));
    }

    public Employee updateEmployee(final Long id, final Employee updatedEmployee) {
        final Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setName(updatedEmployee.getName());
        employee.setEmail(updatedEmployee.getEmail());

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(final Long id) {
        employeeRepository.deleteById(id);
    }
}
