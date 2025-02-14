package com.hiutaleapp.controller;

import com.hiutaleapp.dto.TicketDTO;
import com.hiutaleapp.entity.Favourite;
import com.hiutaleapp.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/all")
    public List<TicketDTO> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public TicketDTO createTicket(@RequestBody Favourite favourite) {
        return ticketService.createTicket(favourite);
    }

    @PutMapping("/update/{id}")
    public TicketDTO updateTicket(@PathVariable Long id, @RequestBody Favourite favourite) {
        return ticketService.updateTicket(id, favourite);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }
}