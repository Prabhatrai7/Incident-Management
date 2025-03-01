package com.incidentmanagement.service;

import com.incidentmanagement.dto.LocationDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocationService {
    private final Map<String, LocationDTO> pincodeMap = new HashMap<>();
    
    public LocationService() {
        pincodeMap.put("110001", new LocationDTO("New Delhi", "Delhi", "India"));
        pincodeMap.put("400001", new LocationDTO("Mumbai", "Maharashtra", "India"));
        pincodeMap.put("700001", new LocationDTO("Kolkata", "West Bengal", "India"));
        pincodeMap.put("600001", new LocationDTO("Chennai", "Tamil Nadu", "India"));
        pincodeMap.put("500001", new LocationDTO("Hyderabad", "Telangana", "India"));
        pincodeMap.put("560001", new LocationDTO("Bangalore", "Karnataka", "India"));
    }
    
    public LocationDTO getLocationByPincode(String pincode) {
        LocationDTO location = pincodeMap.get(pincode);
        if (location == null) {
            return new LocationDTO("Unknown City", "Unknown State", "Unknown Country");
        }
        return location;
    }
}