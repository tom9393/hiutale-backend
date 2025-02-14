package com.hiutaleapp.repository;

import com.hiutaleapp.entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Favourite, Long> {}
