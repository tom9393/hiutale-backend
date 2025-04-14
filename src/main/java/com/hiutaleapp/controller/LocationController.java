package com.hiutaleapp.controller;

import com.hiutaleapp.dto.LocationDTO;
import com.hiutaleapp.entity.Location;
import com.hiutaleapp.service.LocationService;
import com.hiutaleapp.util.UserContext;
import com.hiutaleapp.util.forms.LocationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping("/all")
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public LocationDTO createLocation(@RequestBody LocationForm locationForm) {
        return locationService.createLocation(UserContext.getUserId(), locationForm);
    }

    @PutMapping("/update/{id}")
    public LocationDTO updateLocation(@PathVariable Long id, @RequestBody Location location) {
        return locationService.updateLocation(id, location);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteLocation(@PathVariable Long id) {
        return locationService.deleteLocation(UserContext.getUserId(), id);
    }
}