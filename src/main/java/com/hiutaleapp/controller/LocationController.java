package com.hiutaleapp.controller;

import com.hiutaleapp.dto.LocationDTO;
import com.hiutaleapp.entity.Location;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.service.LocationService;
import com.hiutaleapp.util.DataViolationException;
import com.hiutaleapp.util.DatabaseConnectionException;
import com.hiutaleapp.util.LocationForm;
import com.hiutaleapp.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


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
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            Location location = new Location();
            User user = new User();

            user.setUserId(Long.parseLong(auth.getName()));
            location.setName(locationForm.getName());
            location.setAddress(locationForm.getAddress());
            location.setCity(locationForm.getCity());
            location.setPostalCode(locationForm.getPostal());
            location.setCreator(user);
            return locationService.createLocation(location);
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not create location, similar location already exists"); // Placeholder
        }
    }

//    @PutMapping("/update/{id}")
//    public LocationDTO updateLocation(@PathVariable Long id, @RequestBody Location location) {
//        return locationService.updateLocation(id, location);
//    }

    @DeleteMapping("/delete/{id}")
    public void deleteLocation(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            Optional<LocationDTO> location = locationService.getLocationById(id);
            if (location.isPresent()) {
                if (location.get().getCreatorId() == Long.parseLong(auth.getName())) {
                    locationService.deleteLocation(id);
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
}