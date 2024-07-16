package org.example.basiccrud.service.implementation;
import com.google.common.base.Preconditions;
import org.example.basiccrud.entity.Department;
import org.example.basiccrud.entity.Mapper.DepartmentMapper;
import org.example.basiccrud.entity.dto.DepartmentDto;
import org.example.basiccrud.repository.DepartmentRepository;
import org.example.basiccrud.repository.LocationRepository;
import org.example.basiccrud.service.DepartmentService;
import org.example.basiccrud.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentServiceImplementation implements DepartmentService {
    DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImplementation(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentDto getDepartmentById(UUID id) {
        Preconditions.checkArgument(id != null, "You can't send null id");

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        return DepartmentMapper.INSTANCE.mapToDepartmentDto(department);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream().map(DepartmentMapper.INSTANCE::mapToDepartmentDto).toList();
    }

    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Preconditions.checkArgument(departmentDto.getId() == null, "You can't send id");
        Preconditions.checkArgument(departmentDto.getName() != null, "You can't create department without name");

        departmentRepository.save(DepartmentMapper.INSTANCE.mapToDepartment(departmentDto));

        return departmentDto;
    }

    @Override
    public DepartmentDto updateDepartment(UUID id, DepartmentDto departmentDto) {
        Preconditions.checkArgument(id != null, "You can't send null id");
        Preconditions.checkArgument(departmentDto.getId() == null, "You can't send id");
        Preconditions.checkArgument(departmentDto.getName() != null, "You can't update department without name");

        Department department =  departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        department.setName(departmentDto.getName());

        departmentRepository.save(department);
        return DepartmentMapper.INSTANCE.mapToDepartmentDto(department);
    }

    @Override
    public DepartmentDto getByDepartmentName(String departmentName) {
        Preconditions.checkArgument(departmentName != null, "You can't search without a name");
        Department department = departmentRepository.findByName(departmentName).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        return DepartmentMapper.INSTANCE.mapToDepartmentDto(department);
    }

    @Override
    public void deleteDepartment(UUID id) {
        Preconditions.checkArgument(id != null, "You can't delete department without id");
        departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        departmentRepository.deleteById(id);
    }

}
