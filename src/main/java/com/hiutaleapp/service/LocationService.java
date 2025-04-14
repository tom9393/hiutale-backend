package com.hiutaleapp.service;

import com.hiutaleapp.dto.LocationDTO;
import com.hiutaleapp.entity.Location;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.repository.LocationRepository;
import com.hiutaleapp.util.errors.DataViolationException;
import com.hiutaleapp.util.errors.DatabaseConnectionException;
import com.hiutaleapp.util.forms.LocationForm;
import com.hiutaleapp.util.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

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

    public LocationDTO createLocation(Long userId, LocationForm locationForm) {
        try {
            Location location = new Location();
            User user = new User();

            user.setUserId(userId);
            location.setName(locationForm.getName());
            location.setAddress(locationForm.getAddress());
            location.setCity(locationForm.getCity());
            location.setPostalCode(locationForm.getPostal());
            location.setCreator(user);
            return mapToDTO(locationRepository.save(location));
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not create location, similar location already exists"); // Placeholder
        }
    }

    public LocationDTO updateLocation(Long id, Location location) {
        location.setLocationId(id);
        return mapToDTO(locationRepository.save(location));
    }

    public Boolean deleteLocation(Long userId, Long id) {
        try {
            Optional<LocationDTO> location = getLocationById(id);
            if (location.isPresent()) {
                if (location.get().getCreatorId() == userId) {
                    locationRepository.deleteById(id);
                    return true;
                } else {
                    throw new AuthorizationDeniedException("You do not have permission to delete this location");
                }
            } else {
                throw new NotFoundException("Location with this ID does not exist");
            }
        } catch (DataAccessResourceFailureException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not delete this location because some events are dependent upon it");
        }
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