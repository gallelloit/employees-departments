package com.gallelloit.employees.dto;

public record EmployeeCreateRequest(
        String name,
        String email,
        Long departmentId
) {}
