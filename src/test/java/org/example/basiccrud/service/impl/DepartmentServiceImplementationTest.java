package org.example.basiccrud.service.impl;

import org.example.basiccrud.entity.Department;
import org.example.basiccrud.entity.dto.DepartmentDto;
import org.example.basiccrud.exception.ResourceNotFoundException;
import org.example.basiccrud.repository.DepartmentRepository;
import org.example.basiccrud.service.implementation.DepartmentServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class DepartmentServiceImplementationTest {
    DepartmentServiceImplementation departmentImpl;

    @Mock
    DepartmentRepository departmentRepositoryMock;

    @BeforeEach
    void setUp() {
        MockSettings mockSettings = Mockito.withSettings().defaultAnswer(Mockito.RETURNS_SMART_NULLS);
        departmentRepositoryMock = Mockito.mock(DepartmentRepository.class, mockSettings);

        departmentImpl = new DepartmentServiceImplementation(departmentRepositoryMock);
    }

    @Test
    public void getAll() {
        Department department1 = new Department();
        department1.setUuid(UUID.randomUUID());
        department1.setName("HR");

        Department department2 = new Department();
        department2.setUuid(UUID.randomUUID());
        department2.setName("IT");

        List<Department> departments = Arrays.asList(department1, department2);

        when(departmentRepositoryMock.findAll()).thenReturn(departments);

        List<DepartmentDto> departmentDtos = departmentImpl.getAllDepartments();

        assertAll("Verify department list",
                () -> assertEquals(2, departmentDtos.size()),
                () -> assertEquals("HR", departmentDtos.get(0).getName()),
                () -> assertEquals("IT", departmentDtos.get(1).getName())
        );
    }

    @Test
    public void getById() {
        Department department = new Department();
        UUID uuid = UUID.randomUUID();
        department.setUuid(uuid);
        department.setName("HR");

        when(departmentRepositoryMock.findById(department.getUuid())).thenReturn(Optional.of(department));

        DepartmentDto departmentDto = departmentImpl.getDepartmentById(department.getUuid());

        assertAll("Verify department by id",
                () -> assertEquals(uuid, UUID.fromString(departmentDto.getId())),
                () -> assertEquals("HR", departmentDto.getName())
        );
    }

    @Test
    public void getByNonValidId() {
        UUID uuid = UUID.randomUUID();

        when(departmentRepositoryMock.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            departmentImpl.getDepartmentById(uuid);
        });
    }
}
