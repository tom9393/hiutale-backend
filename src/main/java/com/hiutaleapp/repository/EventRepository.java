package com.hiutaleapp.repository;

import com.hiutaleapp.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e, " +
            "(SELECT COUNT(a) FROM Attendance a WHERE a.event = e), " +
            "(SELECT COUNT(f) FROM Favourite f WHERE f.event = e) " +
            "FROM Event e")
    List<Object[]> findAllEventsWithAttendanceAndFavouritesCount();

    @Query("SELECT e, " +
            "(SELECT COUNT(a) FROM Attendance a WHERE a.event = e), " +
            "(SELECT COUNT(f) FROM Favourite f WHERE f.event = e) " +
            "FROM Event e WHERE e.eventId = :eventId")
    List<Object[]> findEventWithAttendanceAndFavouritesCount(@Param("eventId") Long eventId);
}
