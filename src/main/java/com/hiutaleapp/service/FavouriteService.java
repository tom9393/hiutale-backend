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
public class TicketService {
    @Autowired
    private FavouriteRepository favouriteRepository;

    public List<FavouriteDTO> getAllTickets() {
        return favouriteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<FavouriteDTO> getTicketById(Long id) {
        return favouriteRepository.findById(id).map(this::mapToDTO);
    }

    public FavouriteDTO createTicket(Favourite favourite) {
        return mapToDTO(favouriteRepository.save(favourite));
    }

    public FavouriteDTO updateTicket(Long id, Favourite favourite) {
        favourite.setTicketId(id);
        return mapToDTO(favouriteRepository.save(favourite));
    }

    public void deleteTicket(Long id) {
        favouriteRepository.deleteById(id);
    }

    public FavouriteDTO mapToDTO(Favourite favourite) {
        return new FavouriteDTO(favourite);
    }

    public Favourite mapToEntity(FavouriteDTO favouriteDTO) {
        Favourite favourite = new Favourite();
        favourite.setTicketId(favouriteDTO.getTicketId());
        favourite.getEvent().setEventId(favouriteDTO.getEventId());
        favourite.getUser().setUserId(favouriteDTO.getUserId());
        favourite.setTicketType(favouriteDTO.getTicketType());
        favourite.setPrice(favouriteDTO.getPrice());
        favourite.setStatus(favouriteDTO.getStatus());
        favourite.setCreatedAt(favouriteDTO.getPurchaseDate());
        return favourite;
    }
}