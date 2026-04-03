package com.gallelloit.employees.mapper;

import com.gallelloit.employees.dto.DepartmentDTO;
import com.gallelloit.employees.entity.Department;

public class DepartmentMapper {
    public static DepartmentDTO toDTO(Department department) {
        return DepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }
}
