package com.incidentmanagement.controller;

import com.incidentmanagement.dto.LocationDTO;
import com.incidentmanagement.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/pincode/{pincode}")
    public ResponseEntity<LocationDTO> getLocationByPincode(@PathVariable String pincode) {
        LocationDTO location = locationService.getLocationByPincode(pincode);
        return ResponseEntity.ok(location);
    }
}