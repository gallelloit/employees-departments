package com.gallelloit.employees.service;

import com.gallelloit.employees.entity.Department;
import com.gallelloit.employees.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository repository;

    public List<Department> findAll() {
        return repository.findAll();
    }

    public Department save(Department department) {
        return repository.save(department);
    }
}
