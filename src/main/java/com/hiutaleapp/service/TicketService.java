package com.hiutaleapp.service;

import com.hiutaleapp.dto.TicketDTO;
import com.hiutaleapp.entity.Ticket;
import com.hiutaleapp.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TicketDTO> getTicketById(Long id) {
        return ticketRepository.findById(id).map(this::mapToDTO);
    }

    public TicketDTO createTicket(Ticket ticket) {
        return mapToDTO(ticketRepository.save(ticket));
    }

    public TicketDTO updateTicket(Long id, Ticket ticket) {
        ticket.setTicketId(id);
        return mapToDTO(ticketRepository.save(ticket));
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public TicketDTO mapToDTO(Ticket ticket) {
        return new TicketDTO(ticket);
    }

    public Ticket mapToEntity(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setTicketId(ticketDTO.getTicketId());
        ticket.getEvent().setEventId(ticketDTO.getEventId());
        ticket.getUser().setUserId(ticketDTO.getUserId());
        ticket.setTicketType(ticketDTO.getTicketType());
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setStatus(ticketDTO.getStatus());
        ticket.setCreatedAt(ticketDTO.getPurchaseDate());
        return ticket;
    }
}