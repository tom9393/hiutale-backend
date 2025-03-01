package com.hiutaleapp.controller;

import com.hiutaleapp.dto.FileDTO;
import com.hiutaleapp.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public FileDTO uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("eventId") Long eventId) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return fileService.uploadFile(file, Long.parseLong(auth.getName()), eventId);
    }

    @GetMapping("/one/{fileId}")
    public FileDTO getFileUrl(@PathVariable Long fileId) {
        return fileService.getFileByName(fileId);
    }

    @GetMapping("/event/{eventId}")
    public List<FileDTO> getImagesByEventId(@PathVariable Long eventId) {
        return fileService.getImageNamesByEventId(eventId);
    }

    @DeleteMapping("/delete/{fileId}")
    public void deleteFile(@PathVariable Long fileId) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        fileService.deleteFile(fileId, Long.parseLong(auth.getName()));
    }
}
