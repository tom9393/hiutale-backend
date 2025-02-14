package com.hiutaleapp.repository;

import com.hiutaleapp.entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    Optional<Favourite> findByUser_UserIdAndEvent_EventId(Long userId, Long eventId);

}
