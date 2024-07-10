package org.example.basiccrud.service;
import org.example.basiccrud.entity.dto.DepartmentDto;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DepartmentService {
    public DepartmentDto getDepartmentById(UUID id);
    public List<DepartmentDto> getAllDepartments();
    public DepartmentDto createDepartment(DepartmentDto departmentDto);
    public DepartmentDto updateDepartment(UUID id, DepartmentDto departmentDto);
    public DepartmentDto getByDepartmentName(String departmentName);
    public abstract void deleteDepartment(UUID id);
}
