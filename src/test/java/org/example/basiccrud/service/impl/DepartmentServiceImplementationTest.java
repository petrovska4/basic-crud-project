package org.example.basiccrud.service.impl;

import org.example.basiccrud.entity.Department;
import org.example.basiccrud.entity.Mapper.DepartmentMapper;
import org.example.basiccrud.entity.dto.DepartmentDto;
import org.example.basiccrud.exception.ResourceNotFoundException;
import org.example.basiccrud.repository.DepartmentRepository;
import org.example.basiccrud.service.implementation.DepartmentServiceImplementation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@MockitoSettings
class DepartmentServiceImplementationTest {
    @Mock
    DepartmentRepository departmentRepositoryMock;

    @Mock
    DepartmentMapper departmentMapperMock;

    @InjectMocks
    DepartmentServiceImplementation departmentImpl;

//    @BeforeEach
//    void setUp() {
//        MockSettings mockSettings = Mockito.withSettings().defaultAnswer(Mockito.RETURNS_SMART_NULLS);
//        departmentRepositoryMock = Mockito.mock(DepartmentRepository.class, mockSettings);
//        departmentMapperMock = Mockito.mock(DepartmentMapper.class, Mockito.RETURNS_SMART_NULLS);
//
//        departmentImpl = new DepartmentServiceImplementation(departmentRepositoryMock);
//    }

    // getAllDepartments()
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

        verify(departmentRepositoryMock, times(1)).findAll();

        assertAll("Verify department list",
                () -> assertEquals(2, departmentDtos.size()),
                () -> assertEquals("HR", departmentDtos.get(0).getName()),
                () -> assertEquals("IT", departmentDtos.get(1).getName())
        );
    }

    // getDepartmentById(id)
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

    // getDepartmentById(null)
    @Test
    public void getByNullId() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> departmentImpl.getDepartmentById(null),
                "Expected getDepartmentById() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't send null id", thrown.getMessage());
    }

    // getDepartmentById(id) id - not valid
    @Test
    public void getByNonexistantId() {
        UUID uuid = UUID.randomUUID();

        when(departmentRepositoryMock.findById(uuid)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            departmentImpl.getDepartmentById(uuid);
        });

        assertEquals("Department not found", thrown.getMessage());
    }

    @Test
    public void createDepartment() {
        Department department = new Department();
        UUID uuid = UUID.randomUUID();
        department.setUuid(uuid);
        department.setName("HR");

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setName("HR");

        when(departmentRepositoryMock.save(department)).thenReturn(department);
        when(departmentMapperMock.mapToDepartment(departmentDto)).thenReturn(department);

        DepartmentDto result = departmentImpl.createDepartment(departmentDto);

        assertAll("Create department",
                () -> assertNull(result.getId(), "Department id is null"),
                () -> assertEquals("HR", result.getName())
        );
    }

    @Test
    public void createDepartmentWithId() {
        DepartmentDto departmentDto = new DepartmentDto();
        String id = UUID.randomUUID().toString();
        departmentDto.setId(id);
        departmentDto.setName("HR");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> departmentImpl.createDepartment(departmentDto),
                "Expected createDepartment() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't send id", thrown.getMessage());
    }

    @Test
    public void createDepartmentWithoutName() {
        DepartmentDto departmentDto = new DepartmentDto();

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> departmentImpl.createDepartment(departmentDto),
                "Expected createDepartment() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't create department without name", thrown.getMessage());
    }

    @Test
    public void createDepartmentWithoutNameAndWithId() {
        DepartmentDto departmentDto = new DepartmentDto();
        String id = UUID.randomUUID().toString();
        departmentDto.setId(id);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> departmentImpl.createDepartment(departmentDto),
                "Expected createDepartment() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't send id", thrown.getMessage());
    }

    @Test
    public void updateDepartment() {
        Department department = new Department();
        UUID uuid = UUID.randomUUID();
        department.setUuid(uuid);
        department.setName("HR");

        DepartmentDto departmentDto = new DepartmentDto(); //toa shto se prakja
        departmentDto.setName("HR");

        when(departmentRepositoryMock.findById(uuid)).thenReturn(Optional.of(department));
        when(departmentRepositoryMock.save(department)).thenReturn(department);

        DepartmentDto result = departmentImpl.updateDepartment(uuid, departmentDto);

        assertAll("Update department",
                () -> assertEquals(uuid.toString(), result.getId()),
                () -> assertEquals("HR", result.getName())
        );
    }

    @Test
    public void updateDepartmentWithId() {
        DepartmentDto departmentDto = new DepartmentDto();
        String id = UUID.randomUUID().toString();
        departmentDto.setId(id);
        departmentDto.setName("HR");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> departmentImpl.updateDepartment(UUID.fromString(id), departmentDto),
                "Expected updateDepartment() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't send id", thrown.getMessage());
    }

    @Test
    public void updateDepartmentWithoutName() {
        DepartmentDto departmentDto = new DepartmentDto();
        UUID id = UUID.randomUUID();

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> departmentImpl.updateDepartment(id, departmentDto),
                "Expected createDepartment() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't update department without name", thrown.getMessage());
    }

    // ova ne treba
    @Test
    public void updateDepartmentWithoutNameAndWithId() {
        DepartmentDto departmentDto = new DepartmentDto();
        String id = UUID.randomUUID().toString();
        departmentDto.setId(id);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> departmentImpl.updateDepartment(UUID.fromString(id), departmentDto),
                "Expected updateDepartment() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't send id", thrown.getMessage());
    }

    // ova e test za id koe shto se praka (id, departmentDto), id ne postoi
    @Test
    public void updateDepartmentWithNonexistingParameterId() {
        UUID uuid = UUID.randomUUID();
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setName("HR");

        when(departmentRepositoryMock.findById(uuid)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            departmentImpl.updateDepartment(uuid, departmentDto);
        });

        assertEquals("Department not found", thrown.getMessage());
    }

    // ova e test za id koe shto se praka (null, departmentDto)
    @Test
    public void updateDepartmentWithNullId() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> departmentImpl.updateDepartment(null, new DepartmentDto()),
                "Expected updateDepartment() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't send null id", thrown.getMessage());
    }

    @Test
    public void getDepartmentName() {
        Department department = new Department();
        String name = "HR";
        department.setName(name);
        UUID uuid = UUID.randomUUID();
        department.setUuid(uuid);

        when(departmentRepositoryMock.findByName(name)).thenReturn(Optional.of(department));

        DepartmentDto departmentDto = departmentImpl.getByDepartmentName(name);

        assertEquals(department.getName(), departmentDto.getName());
    }

    @Test
    public void getDepartmentWithoutName() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> departmentImpl.getByDepartmentName(null),
                "Expected getByDepartmentName() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't search without a name", thrown.getMessage());
    }

    @Test
    public void deleteDepartment() {
        UUID uuid = UUID.randomUUID();
        Department department = new Department();
        String name = "HR";
        department.setName(name);
        department.setUuid(uuid);

        when(departmentRepositoryMock.findById(uuid)).thenReturn(Optional.of(department));

        doNothing().when(departmentRepositoryMock).deleteById(uuid);

        departmentImpl.deleteDepartment(uuid);

        verify(departmentRepositoryMock, times(1)).deleteById(uuid);
    }

    @Test
    public void deleteDepartmentWithoutId() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> departmentImpl.deleteDepartment(null),
                "Expected deleteDepartment() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't delete department without id", thrown.getMessage());
    }

    @Test
    public void deleteDepartmentWithNonexistingId() {
        UUID uuid = UUID.randomUUID();

        when(departmentRepositoryMock.findById(uuid)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            departmentImpl.deleteDepartment(uuid);
        });

        assertEquals("Department not found", thrown.getMessage());
    }
}
