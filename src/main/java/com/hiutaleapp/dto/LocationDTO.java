package com.hiutaleapp.dto;

import com.hiutaleapp.entity.Location;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LocationDTO {
    private Long locationId;
    private String name;
    private String address;
    private String city;
    private String postalCode;
    private Date createdAt;
    private Date updatedAt;

    public LocationDTO(Location location) {
        this.locationId = location.getLocationId();
        this.name = location.getName();
        this.address = location.getAddress();
        this.city = location.getCity();
        this.postalCode = location.getPostalCode();
        this.createdAt = location.getCreatedAt();
        this.updatedAt = location.getUpdatedAt();
    }
}