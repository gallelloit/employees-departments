package com.gallelloit.employees.dto;

import lombok.Builder;

@Builder
public record EmployeeDTO(
        Long id,
        String name,
        String email,
        Long departmentId,
        String departmentName
) {}
