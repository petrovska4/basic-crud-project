package org.example.basiccrud.service.impl;

import org.example.basiccrud.entity.Department;
import org.example.basiccrud.entity.Location;
import org.example.basiccrud.entity.Mapper.DepartmentMapper;
import org.example.basiccrud.entity.Mapper.LocationMapper;
import org.example.basiccrud.entity.dto.DepartmentDto;
import org.example.basiccrud.entity.dto.LocationDto;
import org.example.basiccrud.exception.ResourceNotFoundException;
import org.example.basiccrud.repository.LocationRepository;
import org.example.basiccrud.service.DepartmentService;
import org.example.basiccrud.service.implementation.LocationServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
public class LocationServiceImplementationTest {
    @Mock
    LocationRepository locationRepositoryMock;

    @Mock
    DepartmentService departmentServiceMock;

    @Mock
    LocationMapper locationMapperMock;

    @Mock
    DepartmentMapper departmentMapperMock;

    @InjectMocks
    LocationServiceImplementation locationImpl;

//    @BeforeEach
//    void setUp() {
//        MockSettings mockSettings = Mockito.withSettings().defaultAnswer(Mockito.RETURNS_SMART_NULLS);
//        locationRepositoryMock = Mockito.mock(LocationRepository.class, mockSettings);
//        locationMapperMock = Mockito.mock(LocationMapper.class, Mockito.RETURNS_SMART_NULLS);
//        departmentServiceMock = Mockito.mock(DepartmentService.class, mockSettings);
//
//        locationImpl = new LocationServiceImplementation(locationRepositoryMock, departmentServiceMock);
//    }

    @Test
    public void getAll() {
        Department department1 = new Department();
        department1.setUuid(UUID.randomUUID());
        department1.setName("HR");

        Department department2 = new Department();
        department2.setUuid(UUID.randomUUID());
        department2.setName("IT");

        Location location1 = new Location();
        location1.setUuid(UUID.randomUUID());
        location1.setName("HR");
        location1.setDepartment(department1);

        Location location2 = new Location();
        location2.setUuid(UUID.randomUUID());
        location2.setName("IT");
        location2.setDepartment(department2);

        List<Location> locations = Arrays.asList(location1, location2);

        when(locationRepositoryMock.findAll()).thenReturn(locations);

        List<LocationDto> locationDtos = locationImpl.getAllLocations();

        verify(locationRepositoryMock, times(1)).findAll();

        assertAll("Verify location list",
                () -> assertEquals(2, locationDtos.size()),
                () -> assertEquals("HR", locationDtos.get(0).getName()),
                () -> assertEquals("IT", locationDtos.get(1).getName()),
                () -> assertEquals(department1.getUuid(), locationDtos.get(0).getDepartment().getUuid()),
                () -> assertEquals(department2.getUuid(), locationDtos.get(1).getDepartment().getUuid()),
                () -> assertEquals(department1.getName(), locationDtos.get(0).getDepartment().getName()),
                () -> assertEquals(department2.getName(), locationDtos.get(1).getDepartment().getName())
        );
    }

    @Test
    public void getLocationById() {
        Department department = new Department();
        UUID uuid1 = UUID.randomUUID();
        department.setUuid(uuid1);
        department.setName("HR");

        Location location = new Location();
        UUID uuid2 = UUID.randomUUID();
        location.setUuid(uuid2);
        location.setName("IT");
        location.setDepartment(department);

        when(locationRepositoryMock.findById(location.getUuid())).thenReturn(Optional.of(location));

        LocationDto locationDto = locationImpl.getLocationById(location.getUuid());

        verify(locationRepositoryMock, times(1)).findById(uuid2);

        assertAll("Verify location by id",
                () -> assertEquals("IT", locationDto.getName()),
                () -> assertEquals(department.getUuid(), locationDto.getDepartment().getUuid()),
                () -> assertEquals(department.getName(), locationDto.getDepartment().getName())
        );
    }

    @Test
    public void getByNullId() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.getLocationById(null),
                "Expected getLocationById() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't send null id", thrown.getMessage());
    }

    @Test
    public void getByNonexistantId() {
        UUID uuid = UUID.randomUUID();

        when(locationRepositoryMock.findById(uuid)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            locationImpl.getLocationById(uuid);
        });

        assertEquals("No location found", thrown.getMessage());
    }

    @Test
    public void createLocation() {
        Department department = new Department();
        department.setName("HR");

        Location location = new Location();
        location.setName("IT");
        location.setDepartment(department);

        LocationDto locationDto = new LocationDto();
        locationDto.setName("IT");
        locationDto.setDepartment(department);

        DepartmentDto departmentDto = new DepartmentDto();
        department.setName("HR");

        when(departmentServiceMock.getByDepartmentName(department.getName())).thenReturn(departmentDto);
        when(locationRepositoryMock.save(any(Location.class))).thenAnswer(invocation -> {
            Location savedLocation = invocation.getArgument(0);
            savedLocation.setUuid(UUID.randomUUID()); // Mock a UUID for location
            savedLocation.setName(location.getName());
            savedLocation.setDepartment(department);
            return savedLocation;
        });
//        when(locationMapperMock.mapToLocation(locationDto)).thenReturn(location);
//        when(locationMapperMock.mapToLocationDto(location)).thenReturn(locationDto);
//        when(departmentMapperMock.mapToDepartment(departmentDto)).thenReturn(department);

        LocationDto result = locationImpl.createLocation(locationDto);

        assertAll("Create location",
                () -> assertEquals(result.getName(), locationDto.getName()),
                () -> assertEquals(result.getDepartment().getUuid(), locationDto.getDepartment().getUuid()),
                () -> assertEquals(result.getDepartment().getName(), locationDto.getDepartment().getName())
        );
    }

    @Test
    public void createLocationWithLocationId() {
        Department department = new Department();
        department.setName("HR");

        LocationDto locationDto = new LocationDto();
        locationDto.setId(UUID.randomUUID().toString());
        locationDto.setName("IT");
        locationDto.setDepartment(department);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.createLocation(locationDto),
                "Expected createLocation() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't send location id", thrown.getMessage());
    }

    @Test
    public void createLocationWithoutName() {
        LocationDto locationDto = new LocationDto();

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.createLocation(locationDto),
                "Expected createLocation() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't create location with null name", thrown.getMessage());
    }

    @Test
    public void createLocationWithoutDepartment() {
        LocationDto locationDto = new LocationDto();
        locationDto.setName("IT");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.createLocation(locationDto),
                "Expected createLocation() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't create location with null department", thrown.getMessage());
    }

    @Test
    public void createLocationWithDepartmentId() {
        Department department = new Department();
        department.setUuid(UUID.randomUUID());
        department.setName("HR");

        LocationDto locationDto = new LocationDto();
        locationDto.setName("IT");
        locationDto.setDepartment(department);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.createLocation(locationDto),
                "Expected createLocation() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't send department id", thrown.getMessage());
    }

    @Test
    public void createLocationWithoutDepartmentName() {
        Department department = new Department();

        LocationDto locationDto = new LocationDto();
        locationDto.setName("IT");
        locationDto.setDepartment(department);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.createLocation(locationDto),
                "Expected createLocation() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't create location without department name", thrown.getMessage());
    }

    @Test
    public void updateLocation() {
        Department department = new Department();
        department.setName("HR");

        Location location = new Location();
        location.setName("IT");
        location.setDepartment(department);

        LocationDto locationDto = new LocationDto();
        locationDto.setName("IT");
        locationDto.setDepartment(department);

        DepartmentDto departmentDto = new DepartmentDto();
        department.setName("HR");

        when(departmentServiceMock.getByDepartmentName(department.getName())).thenReturn(departmentDto);
        when(locationRepositoryMock.save(any(Location.class))).thenAnswer(invocation -> {
            Location savedLocation = invocation.getArgument(0);
            savedLocation.setUuid(UUID.randomUUID()); // Mock a UUID for location
            savedLocation.setName(location.getName());
            savedLocation.setDepartment(department);
            return savedLocation;
        });
        LocationDto result = locationImpl.createLocation(locationDto);

        assertAll("Create location",
                () -> assertEquals(result.getName(), locationDto.getName()),
                () -> assertEquals(result.getDepartment().getUuid(), locationDto.getDepartment().getUuid()),
                () -> assertEquals(result.getDepartment().getName(), locationDto.getDepartment().getName())
        );
    }

    @Test
    public void updateLocationWithLocationId() {
        Department department = new Department();
        department.setName("HR");

        LocationDto locationDto = new LocationDto();
        UUID uuid = UUID.randomUUID();
        locationDto.setId(String.valueOf(uuid));
        locationDto.setName("IT");
        locationDto.setDepartment(department);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.updateLocation(uuid, locationDto),
                "Expected createLocation() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't send location id", thrown.getMessage());
    }

    @Test
    public void updateLocationWithoutId() {
        Department department = new Department();
        department.setName("HR");

        LocationDto locationDto = new LocationDto();
        locationDto.setName("IT");
        locationDto.setDepartment(department);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.updateLocation(null, locationDto),
                "Expected createLocation() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't send location without id", thrown.getMessage());
    }

    @Test
    public void updateLocationWithoutLocationName() {
        Department department = new Department();
        department.setName("HR");

        LocationDto locationDto = new LocationDto();
        UUID uuid = UUID.randomUUID();
        locationDto.setDepartment(department);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.updateLocation(uuid, locationDto),
                "Expected createLocation() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't update location with null name", thrown.getMessage());
    }

    @Test
    public void updateLocationWithoutDepartment() {
        LocationDto locationDto = new LocationDto();
        UUID uuid = UUID.randomUUID();
        locationDto.setName("IT");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.updateLocation(uuid, locationDto),
                "Expected createLocation() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't update location with null department", thrown.getMessage());
    }

    @Test
    public void updateLocationWithDepartmentId() {
        UUID uuid = UUID.randomUUID();
        Department department = new Department();
        department.setName("HR");
        department.setUuid(uuid);

        LocationDto locationDto = new LocationDto();
        locationDto.setName("IT");
        locationDto.setDepartment(department);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.updateLocation(uuid, locationDto),
                "Expected createLocation() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't update department id", thrown.getMessage());
    }

    @Test
    public void updateLocationWithDepartmentName() {
        UUID uuid = UUID.randomUUID();
        Department department = new Department();

        LocationDto locationDto = new LocationDto();
        locationDto.setName("IT");
        locationDto.setDepartment(department);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.updateLocation(uuid, locationDto),
                "Expected createLocation() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't update location without department name", thrown.getMessage());
    }

    @Test
    public void updateLocationWithNonexistingLocationId() {
        UUID uuid = UUID.randomUUID();
        Department department = new Department();
        department.setName("HR");

        LocationDto locationDto = new LocationDto();
        locationDto.setName("IT");
        locationDto.setDepartment(department);

        Location location = new Location();
        location.setUuid(uuid);
        location.setName("IT");
        location.setDepartment(department);

        when(locationRepositoryMock.findById(uuid)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            locationImpl.updateLocation(uuid, locationDto);
        });

        assertEquals("No location found", thrown.getMessage());
    }

    @Test
    public void updateLocationWithNonexistingDepartmentName() {
        UUID uuid = UUID.randomUUID();
        Department department = new Department();
        department.setName("HR");

        LocationDto locationDto = new LocationDto();
        locationDto.setName("IT");
        locationDto.setDepartment(department);

        Location location = new Location();
        location.setUuid(uuid);
        location.setName("IT");
        location.setDepartment(department);

        when(locationRepositoryMock.findById(uuid)).thenReturn(Optional.of(location));
        when(departmentServiceMock.getByDepartmentName(department.getName())).thenReturn(null);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            locationImpl.updateLocation(uuid, locationDto);
        });

        assertEquals("Department not found", thrown.getMessage());
    }

    @Test
    public void deleteLocation() {
        UUID uuid = UUID.randomUUID();
        Department department = new Department();
        department.setName("HR");

        Location location = new Location();
        location.setUuid(uuid);
        location.setName("IT");
        location.setDepartment(department);

        when(locationRepositoryMock.findById(uuid)).thenReturn(Optional.of(location));

        doNothing().when(locationRepositoryMock).deleteById(uuid);

        locationImpl.deleteLocation(uuid);

        verify(locationRepositoryMock, times(1)).deleteById(uuid);
    }

    @Test
    public void deleteLocationWithoutId() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> locationImpl.deleteLocation(null),
                "Expected deleteLocation() to throw an IllegalArgumentException, but it didn't"
        );

        assertEquals("You can't delete location without id", thrown.getMessage());
    }

    @Test
    public void deleteLocationWithNonexistingId() {
        UUID uuid = UUID.randomUUID();

        when(locationRepositoryMock.findById(uuid)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            locationImpl.deleteLocation(uuid);
        });

        assertEquals("Location not found", thrown.getMessage());
    }


}
