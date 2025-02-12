package com.hiutaleapp.service;

import com.hiutaleapp.dto.LocationDTO;
import com.hiutaleapp.entity.Location;
import com.hiutaleapp.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<LocationDTO> getLocationById(Long id) {
        return locationRepository.findById(id).map(this::mapToDTO);
    }

    public LocationDTO createLocation(Location location) {
        return mapToDTO(locationRepository.save(location));
    }

    public LocationDTO updateLocation(Long id, Location location) {
        location.setLocationId(id);
        return mapToDTO(locationRepository.save(location));
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    public LocationDTO mapToDTO(Location location) {
        return new LocationDTO(location);
    }

    public Location mapToEntity(LocationDTO locationDTO) {
        Location location = new Location();
        location.setLocationId(locationDTO.getId());
        location.setName(locationDTO.getName());
        location.setAddress(locationDTO.getAddress());
        location.setCity(locationDTO.getCity());
        location.setPostalCode(locationDTO.getPostalCode());
        location.setCreatedAt(locationDTO.getCreatedAt());
        location.setUpdatedAt(locationDTO.getUpdatedAt());
        return location;
    }
}