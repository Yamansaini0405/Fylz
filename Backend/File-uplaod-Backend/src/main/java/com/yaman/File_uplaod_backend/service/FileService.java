package com.yaman.File_uplaod_backend.service;



import com.yaman.File_uplaod_backend.model.FileEntity;
import com.yaman.File_uplaod_backend.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final S3Service s3Service;

    public FileEntity uploadFile(MultipartFile file, String title, String description, String username) throws Exception {
        String fileUrl = s3Service.uploadFile(file);

        FileEntity fileEntity = FileEntity.builder()
                .title(title)
                .description(description)
                .fileName(file.getOriginalFilename())
                .fileUrl(fileUrl)
                .uploadedBy(username)
                .uploadTime(LocalDateTime.now())
                .build();

        return fileRepository.save(fileEntity);
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    public List<FileEntity> searchFiles(String keyword) {
        return fileRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<FileEntity> getUserFiles(String username) {
        return fileRepository.findByUploadedBy(username);
    }

    public FileEntity getFileById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }
}

