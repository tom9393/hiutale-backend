package com.hiutaleapp.service;

import com.hiutaleapp.dto.FavouriteDTO;
import com.hiutaleapp.entity.Favourite;
import com.hiutaleapp.repository.FavouriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<FavouriteDTO> getFavouriteByUserIdAndFavouriteId(Long userId, Long favouriteId) {
        return favouriteRepository.findByUser_UserIdAndEvent_EventId(userId, favouriteId).map(FavouriteDTO::new);
    }

    public FavouriteDTO createFavourite(Favourite favourite) {
        return mapToDTO(favouriteRepository.save(favourite));
    }

    public FavouriteDTO updateFavourite(Long id, Favourite favourite) {
        favourite.setFavouriteId(id);
        return mapToDTO(favouriteRepository.save(favourite));
    }

    public void deleteFavourite(Long id) {
        favouriteRepository.deleteById(id);
    }

    public FavouriteDTO mapToDTO(Favourite favourite) {
        return new FavouriteDTO(favourite);
    }

    public Favourite mapToEntity(FavouriteDTO favouriteDTO) {
        Favourite favourite = new Favourite();
        return favourite;
    }
}