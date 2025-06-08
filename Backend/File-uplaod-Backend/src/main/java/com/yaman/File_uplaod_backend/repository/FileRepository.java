package com.yaman.File_uplaod_backend.repository;



import com.yaman.File_uplaod_backend.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByUploadedBy(String username); // For filtering files per user if needed

    List<FileEntity> findByTitleContainingIgnoreCase(String keyword);

    List<FileEntity> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}

