package com.yaman.File_uplaod_backend.controller;



import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.yaman.File_uplaod_backend.model.FileEntity;
import com.yaman.File_uplaod_backend.model.User;
import com.yaman.File_uplaod_backend.repository.FileRepository;
import com.yaman.File_uplaod_backend.repository.UserRepository;
import com.yaman.File_uplaod_backend.service.FileService;
import com.yaman.File_uplaod_backend.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@CrossOrigin
public class FileController {

    private final FileService fileService;

    private final S3Service s3Service;

    private final AmazonS3 amazonS3;

    private final FileRepository fileRepository;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

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

    @GetMapping("/generate-presigned-url")
    public ResponseEntity<String> generatePresignedUrl(@RequestParam String fileName) {

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)); // 5 minutes

        URL url = amazonS3.generatePresignedUrl(request);
        return ResponseEntity.ok(url.toString());
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String key) throws IOException {
//        String decodedKey = URLDecoder.decode(key, StandardCharsets.UTF_8);
//        System.out.println("Decoded Key: [" + decodedKey.trim() + "]");

//        String safeFilename = decodedKey.replaceAll("[\\r\\n\"]", "_");

        S3ObjectInputStream s3InputStream = s3Service.downloadFile(key.trim());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(s3InputStream));
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long fileId, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<FileEntity> fileOptional = fileRepository.findById(fileId);

        if (!fileOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }

        FileEntity file = fileOptional.get();

        // Verify the file belongs to the current user
//        if (!file.getUploadedBy().equals(userDetails.getUsername())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this file");
//        }

        String url = file.getFileUrl();
        fileRepository.delete(file);
        s3Service.deleteFile(url.substring(url.lastIndexOf('/') + 1));

        return ResponseEntity.ok("File deleted successfully");
    }



}

