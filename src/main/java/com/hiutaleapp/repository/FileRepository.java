package com.hiutaleapp.repository;

import com.hiutaleapp.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByEvent_EventId(Long eventId);

}
