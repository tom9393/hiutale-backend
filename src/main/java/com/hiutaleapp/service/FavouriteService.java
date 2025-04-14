package com.hiutaleapp.service;

import com.hiutaleapp.dto.FavouriteDTO;
import com.hiutaleapp.entity.Event;
import com.hiutaleapp.entity.Favourite;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.repository.FavouriteRepository;
import com.hiutaleapp.util.errors.DataViolationException;
import com.hiutaleapp.util.errors.DatabaseConnectionException;
import com.hiutaleapp.util.forms.FavouriteForm;
import com.hiutaleapp.util.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavouriteService {
    @Autowired
    private FavouriteRepository favouriteRepository;

    public List<FavouriteDTO> getAllFavourites() {
        return favouriteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<FavouriteDTO> getFavouriteById(Long id) {
        return favouriteRepository.findById(id).map(this::mapToDTO);
    }

    public List<FavouriteDTO> getFavouriteByUser(Long id) {
        return favouriteRepository.findByUser_UserId(id).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<FavouriteDTO> getFavouriteByUserIdAndFavouriteId(Long userId, Long favouriteId) {
        return favouriteRepository.findByUser_UserIdAndEvent_EventId(userId, favouriteId).map(FavouriteDTO::new);
    }

    public FavouriteDTO createFavourite(Long userId, FavouriteForm favouriteForm) {
        try {
            Favourite favourite = new Favourite();
            User user = new User();
            Event event = new Event();

            user.setUserId(userId);
            event.setEventId(favouriteForm.getId());

            favourite.setUser(user);
            favourite.setEvent(event);

            return mapToDTO(favouriteRepository.save(favourite));
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not create review due to foreign key error; either event does not exist or you have already favourited it");
        }
    }

    public FavouriteDTO updateFavourite(Long id, Favourite favourite) {
        favourite.setFavouriteId(id);
        return mapToDTO(favouriteRepository.save(favourite));
    }

    public Boolean deleteFavourite(Long userId, Long id) {
        try {
            Optional<FavouriteDTO> review = getFavouriteByUserIdAndFavouriteId(userId, id);
            if (review.isPresent()) {
                favouriteRepository.deleteById(id);
                return true;
            } else {
                throw new NotFoundException("Event with this ID does not exist");
            }
        } catch (DataAccessResourceFailureException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not delete this review because other data is dependent upon it");
        }
    }

    public FavouriteDTO mapToDTO(Favourite favourite) {
        return new FavouriteDTO(favourite);
    }

    public Favourite mapToEntity(FavouriteDTO favouriteDTO) {
        Favourite favourite = new Favourite();
        return favourite;
    }
}