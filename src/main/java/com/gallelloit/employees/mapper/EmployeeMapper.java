package com.gallelloit.employees.mapper;

import com.gallelloit.employees.dto.EmployeeDTO;
import com.gallelloit.employees.entity.Employee;

public class EmployeeMapper {

    public static EmployeeDTO toDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .departmentId(
                        employee.getDepartment() != null ? employee.getDepartment().getId() : null
                )
                .departmentName(
                        employee.getDepartment() != null ? employee.getDepartment().getName() : null
                )
                .build();
    }
}
