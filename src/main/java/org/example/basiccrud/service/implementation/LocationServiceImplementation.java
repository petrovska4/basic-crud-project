package org.example.basiccrud.service.implementation;
import com.google.common.base.Preconditions;

import org.example.basiccrud.entity.Department;
import org.example.basiccrud.entity.Location;
import org.example.basiccrud.entity.Mapper.DepartmentMapper;
import org.example.basiccrud.entity.Mapper.LocationMapper;
import org.example.basiccrud.entity.dto.DepartmentDto;
import org.example.basiccrud.entity.dto.LocationDto;
import org.example.basiccrud.repository.LocationRepository;
import org.example.basiccrud.service.DepartmentService;
import org.example.basiccrud.service.LocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;

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
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResolutionException("No location found"));
        return LocationMapper.INSTANCE.mapToLocationDto(location);
    }

    @Override
    public List<LocationDto> getAllLocations() {
        return locationRepository.findAll().stream().map(LocationMapper.INSTANCE::mapToLocationDto).toList();
    }

    @Override
    public LocationDto createLocation(LocationDto locationDto) {
        Preconditions.checkArgument(locationDto.getId() == null, "You cant send id");
        Preconditions.checkArgument(locationDto.getDepartment().getUuid() == null, "You cant send id");

        DepartmentDto departmentDto = departmentService.getByDepartmentName(locationDto.getDepartment().getName());

        Location location = new Location();

        location.setDepartment(DepartmentMapper.INSTANCE.mapToDepartment(departmentDto));
        location.setName(locationDto.getName());

        locationRepository.save(location);

        return LocationMapper.INSTANCE.mapToLocationDto(location);
    }

    @Override
    public LocationDto updateLocation(UUID id, LocationDto locationDto) {
        Preconditions.checkArgument(locationDto.getId() == null, "You cant send id");
        Preconditions.checkArgument(locationDto.getDepartment().getUuid() == null, "You cant send id");

        Location location = locationRepository.findById(id).orElseThrow(() -> new ResolutionException("No location found"));

        Department department = DepartmentMapper.INSTANCE.mapToDepartment(departmentService.getByDepartmentName(locationDto.getDepartment().getName()));

        location.setName(locationDto.getName());
        location.setDepartment(department);

        locationRepository.save(location);

        return locationDto;
    }

    @Override
    public void deleteLocation(UUID id) {
        locationRepository.deleteById(id);
    }
}
