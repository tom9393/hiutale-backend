package com.hiutaleapp.service;

import com.hiutaleapp.dto.FileDTO;
import com.hiutaleapp.entity.Event;
import com.hiutaleapp.entity.FileEntity;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.repository.EventRepository;
import com.hiutaleapp.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private EventRepository eventRepository;

    private final String UPLOAD_DIR = "/var/www/img";
    private final List<String> ALLOWED_TYPES = Arrays.asList("image/jpeg", "image/png");
    private final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public FileDTO uploadFile(MultipartFile file, Long userId, Long eventId) throws IOException {
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IOException("Only JPEG and PNG formats are allowed.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File size exceeds 10MB limit.");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String randomizedFilename = UUID.randomUUID() + extension;

        Path filePath = Paths.get(UPLOAD_DIR, randomizedFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING); // Save to path i.e /var/www/img

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(randomizedFilename);
        User user = new User();
        user.setUserId(userId);
        fileEntity.setUser(user);
        fileEntity.setEvent(event);
        return mapToDTO(fileRepository.save(fileEntity));
    }

    public FileDTO getFileByName(Long fileId) {
        Optional<FileEntity> fileEntity = fileRepository.findById(fileId);
        return fileEntity.map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));
    }

    public List<FileDTO> getImageNamesByEventId(Long eventId) {
        List<FileEntity> images = fileRepository.findByEvent_EventId(eventId);

        if (images.isEmpty()) {
            throw new IllegalArgumentException("File not found");
        }
        return images.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Boolean deleteFile(Long userId, Long fileId) throws IOException, IllegalArgumentException, SecurityException {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));

        if (!fileEntity.getUser().getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this file");
        }

        fileRepository.delete(fileEntity);

        Path filePath = Paths.get(UPLOAD_DIR, fileEntity.getFilename());
        Files.deleteIfExists(filePath);
        return true;
    }

    public FileDTO mapToDTO(FileEntity fileEntity) {
        return new FileDTO(fileEntity);
    }
}
