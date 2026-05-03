package com.gallelloit.employees.controller;

import com.gallelloit.employees.dto.EmployeeCreateRequest;
import com.gallelloit.employees.dto.EmployeeDTO;
import com.gallelloit.employees.dto.EmployeeUpdateRequest;
import com.gallelloit.employees.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> save(@RequestBody EmployeeCreateRequest employeeRequest) {
        return ResponseEntity.ok(service.create(employeeRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeUpdateRequest updateRequest) {
        return ResponseEntity.ok(service.updateEmployee(id, updateRequest));
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
    }
}
