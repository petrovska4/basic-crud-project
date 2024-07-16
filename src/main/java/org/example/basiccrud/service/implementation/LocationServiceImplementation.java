package org.example.basiccrud.service.implementation;
import com.google.common.base.Preconditions;

import org.example.basiccrud.entity.Department;
import org.example.basiccrud.entity.Location;
import org.example.basiccrud.entity.Mapper.DepartmentMapper;
import org.example.basiccrud.entity.Mapper.LocationMapper;
import org.example.basiccrud.entity.dto.DepartmentDto;
import org.example.basiccrud.entity.dto.LocationDto;
import org.example.basiccrud.exception.ResourceNotFoundException;
import org.example.basiccrud.repository.LocationRepository;
import org.example.basiccrud.service.DepartmentService;
import org.example.basiccrud.service.LocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class LocationServiceImplementation implements LocationService {
    DepartmentService departmentService;
    LocationRepository locationRepository;

    @Autowired
    public LocationServiceImplementation(LocationRepository locationRepository, DepartmentService departmentService) {
        this.locationRepository = locationRepository;
        this.departmentService = departmentService;
    }

    @Override
    public LocationDto getLocationById(UUID id) {
        Preconditions.checkArgument(id != null, "You can't send null id");

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No location found"));
        return LocationMapper.INSTANCE.mapToLocationDto(location);
    }

    @Override
    public List<LocationDto> getAllLocations() {
        return locationRepository.findAll().stream().map(LocationMapper.INSTANCE::mapToLocationDto).toList();
    }

    @Override
    public LocationDto createLocation(LocationDto locationDto) {
        Preconditions.checkArgument(locationDto.getId() == null, "You can't send location id");
        Preconditions.checkArgument(locationDto.getName() != null, "You can't create location with null name");
        Preconditions.checkArgument(locationDto.getDepartment() != null, "You can't create location with null department");
        Preconditions.checkArgument(locationDto.getDepartment().getUuid() == null, "You can't send department id");
        Preconditions.checkArgument(locationDto.getDepartment().getName() != null, "You can't create location without department name");

        DepartmentDto departmentDto = departmentService.getByDepartmentName(locationDto.getDepartment().getName());

        Location location = new Location();

        location.setDepartment(DepartmentMapper.INSTANCE.mapToDepartment(departmentDto));
        location.setName(locationDto.getName());

        locationRepository.save(location);

        return LocationMapper.INSTANCE.mapToLocationDto(location);
    }

    @Override
    public LocationDto updateLocation(UUID id, LocationDto locationDto) {
        Preconditions.checkArgument(locationDto.getId() == null, "You can't send location id");
        Preconditions.checkArgument(id != null, "You can't send location without id");
        Preconditions.checkArgument(locationDto.getName() != null, "You can't update location with null name");
        Preconditions.checkArgument(locationDto.getDepartment() != null, "You can't update location with null department");
        Preconditions.checkArgument(locationDto.getDepartment().getUuid() == null, "You can't update department id");
        Preconditions.checkArgument(locationDto.getDepartment().getName() != null, "You can't update location without department name");

        Location location = locationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No location found"));

        DepartmentDto departmentDto = departmentService.getByDepartmentName(locationDto.getDepartment().getName());

        if (departmentDto == null) {
            throw new ResourceNotFoundException("Department not found");
        }

        Department department = DepartmentMapper.INSTANCE.mapToDepartment(departmentDto);

        location.setName(locationDto.getName());
        location.setDepartment(department);

        locationRepository.save(location);

        return locationDto;
    }

    @Override
    public void deleteLocation(UUID id) {
        Preconditions.checkArgument(id != null, "You can't delete location without id");
        locationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location not found"));
        locationRepository.deleteById(id);
    }
}
