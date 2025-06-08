package com.yaman.File_uplaod_backend.controller;



import com.yaman.File_uplaod_backend.model.FileEntity;
import com.yaman.File_uplaod_backend.model.User;
import com.yaman.File_uplaod_backend.repository.UserRepository;
import com.yaman.File_uplaod_backend.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@CrossOrigin
public class FileController {

    private final FileService fileService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/upload")
    public ResponseEntity<FileEntity> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            Authentication authentication
    ) throws Exception {
        String email = authentication.getName();
        String username = userRepository.findByEmail(email)
                .map(User::getName)   // get the name if user is present
                .orElse(null);
        FileEntity uploadedFile = fileService.uploadFile(file, title, description, username);
        return ResponseEntity.ok(uploadedFile);
    }
    @GetMapping("/all")
    public ResponseEntity<List<FileEntity>> allFiles(){
        return ResponseEntity.ok(fileService.getAllFiles());
    }

    @GetMapping("/search")
    public ResponseEntity<List<FileEntity>> searchFiles(@RequestParam("q") String keyword) {
        return ResponseEntity.ok(fileService.searchFiles(keyword));
    }

    @GetMapping("/my-files")
    public ResponseEntity<List<FileEntity>> getMyFiles(Authentication authentication) {
        String email = authentication.getName();
        String username = userRepository.findByEmail(email)
                .map(User::getName)   // get the name if user is present
                .orElse(null);
//        String username = authentication.getName();
        return ResponseEntity.ok(fileService.getUserFiles(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileEntity> getFileDetails(@PathVariable Long id) {
        FileEntity file = fileService.getFileById(id);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(file);
    }
}

