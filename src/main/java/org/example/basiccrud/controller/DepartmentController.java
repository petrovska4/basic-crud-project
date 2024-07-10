package org.example.basiccrud.controller;

import org.example.basiccrud.entity.dto.DepartmentDto;
import org.example.basiccrud.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/{id}")
    public DepartmentDto getDepartment(@PathVariable String id) {
        return departmentService.getDepartmentById(UUID.fromString(id));
    }

    @GetMapping
    public List<DepartmentDto> getDepartments() {
        return departmentService.getAllDepartments();
    }

    @PostMapping
    public DepartmentDto createDepartment(@RequestBody DepartmentDto departmentDto) {
        return departmentService.createDepartment(departmentDto);
    }

    @PutMapping("/{id}")
    public DepartmentDto updateDepartment(@PathVariable String id, @RequestBody DepartmentDto departmentDto) {
        return departmentService.updateDepartment(UUID.fromString(id), departmentDto);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable String id) {
        departmentService.deleteDepartment(UUID.fromString(id));
    }

}
