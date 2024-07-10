package org.example.basiccrud.controller;

import org.example.basiccrud.entity.dto.LocationDto;
import org.example.basiccrud.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/{id}")
    public LocationDto getLocation(@PathVariable String id) {
        return locationService.getLocationById(UUID.fromString(id));
    }

    @GetMapping
    public List<LocationDto> getLocations() {
        return locationService.getAllLocations();
    }

    @PostMapping
    public LocationDto createLocation(@RequestBody LocationDto locationDto) {
        return locationService.createLocation(locationDto);
    }

    @PutMapping("/{id}")
    public LocationDto updateLocation(@PathVariable String id, @RequestBody LocationDto locationDto) {
        return locationService.updateLocation(UUID.fromString(id), locationDto);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable String id) {
        locationService.deleteLocation(UUID.fromString(id));
    }
}
