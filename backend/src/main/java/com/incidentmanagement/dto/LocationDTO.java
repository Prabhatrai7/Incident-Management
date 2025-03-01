package com.incidentmanagement.dto;

public class LocationDTO {
    private String city;
    private String state;
    private String country;

    public LocationDTO(String city, String state, String country) {
        this.city = city;
        this.state = state;
        this.country = country;
    }

    // Getters and Setters
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}