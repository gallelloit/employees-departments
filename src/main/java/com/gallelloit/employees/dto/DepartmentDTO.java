package com.gallelloit.employees.dto;

import lombok.Builder;

@Builder
    public record DepartmentDTO(
            Long id,
            String name
    ) {}
