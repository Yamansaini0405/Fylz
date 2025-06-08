package com.yaman.File_uplaod_backend.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadResponse {
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String filename;
    private String contentType;
    private Long size;
    private String uploadedBy;
    private String uploadedAt;
}

