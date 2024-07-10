package org.example.basiccrud.service;
import org.example.basiccrud.entity.dto.LocationDto;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface LocationService {
    public LocationDto getLocationById(UUID id);
    public List<LocationDto> getAllLocations();
    public LocationDto createLocation(LocationDto locationDto);
    public LocationDto updateLocation(UUID id, LocationDto locationDto);
    public void deleteLocation(UUID id);

}
