package com.gallelloit.employees.service;

import com.gallelloit.employees.dto.DepartmentDTO;
import com.gallelloit.employees.entity.Department;
import com.gallelloit.employees.mapper.DepartmentMapper;
import com.gallelloit.employees.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository repository;

    public List<DepartmentDTO> findAll() {

        return repository.findAll().stream().map(DepartmentMapper::toDTO).toList();
    }

    public Department save(Department department) {
        return repository.save(department);
    }
}
