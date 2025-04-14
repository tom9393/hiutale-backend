package com.hiutaleapp.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationForm {

    private String name;
    private String address;
    private String city;
    private String postal;

    public LocationForm(String name, String address, String city, String postal) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.postal = postal;
    }
}
