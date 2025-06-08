package com.yaman.File_uplaod_backend.service;



import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {
        String uniqueFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(new PutObjectRequest(bucketName, uniqueFilename, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.BucketOwnerFullControl));

        return amazonS3.getUrl(bucketName, uniqueFilename).toString();
    }

    public S3ObjectInputStream downloadFile(String key) {
        S3Object object = amazonS3.getObject(bucketName, key);
        return object.getObjectContent();
    }

    public void deleteFile(String key) {
        amazonS3.deleteObject(bucketName, key);
    }
}

