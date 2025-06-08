package com.yaman.File_uplaod_backend.dto;



import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileMetadataResponse {
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String filename;
    private String contentType;
    private Long size;
    private String uploadedAt;
}

