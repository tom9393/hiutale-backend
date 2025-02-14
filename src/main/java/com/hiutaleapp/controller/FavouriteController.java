package com.hiutaleapp.controller;

import com.hiutaleapp.dto.FavouriteDTO;
import com.hiutaleapp.entity.Event;
import com.hiutaleapp.entity.Favourite;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.service.FavouriteService;
import com.hiutaleapp.util.DataViolationException;
import com.hiutaleapp.util.DatabaseConnectionException;
import com.hiutaleapp.util.FavouriteForm;
import com.hiutaleapp.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favourites")
public class FavouriteController {
    @Autowired
    private FavouriteService favouriteService;

    @GetMapping("/all")
    public List<FavouriteDTO> getAllFavourites() {
        return favouriteService.getAllFavourites();
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<FavouriteDTO> getFavouriteById(@PathVariable Long id) {
        return favouriteService.getFavouriteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public FavouriteDTO createFavourite(@RequestBody FavouriteForm favouriteForm) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Favourite favourite = new Favourite();
            User user = new User();
            Event event = new Event();

            user.setUserId(Long.parseLong(auth.getName()));
            event.setEventId(favouriteForm.getId());

            favourite.setUser(user);
            favourite.setEvent(event);

            return favouriteService.createFavourite(favourite);
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not create review due to foreign key error; either event does not exist or you have already favourited it");
        }
    }

    @PutMapping("/update/{id}")
    public FavouriteDTO updateFavourite(@PathVariable Long id, @RequestBody Favourite favourite) {
        return favouriteService.updateFavourite(id, favourite);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFavourite(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            Optional<FavouriteDTO> review = favouriteService.getFavouriteByUserIdAndFavouriteId(Long.parseLong(auth.getName()), id);
            if (review.isPresent()) {
                favouriteService.deleteFavourite(review.get().getId());
            } else {
                throw new NotFoundException("Event with this ID does not exist");
            }
        } catch (DataAccessResourceFailureException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not delete this review because other data is dependent upon it");
        }
    }
}