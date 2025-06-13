package com.yaman.File_uplaod_backend.service;



import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;


    private final String bucketName = "file-uplaods";

    public String uploadFile(MultipartFile file) throws IOException {
        String uniqueFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        System.out.println(uniqueFilename);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(new PutObjectRequest(bucketName, uniqueFilename, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.BucketOwnerFullControl));

        return amazonS3.getUrl(bucketName, uniqueFilename).toString();
    }

    public S3ObjectInputStream downloadFile(String key) {

        S3Object object = amazonS3.getObject(bucketName, key);
        System.out.println(object.getObjectContent());
        return object.getObjectContent();
    }

    public void deleteFile(String key) {
        String decodedKey = URLDecoder.decode(key, StandardCharsets.UTF_8);
        try {
            amazonS3.deleteObject(bucketName, decodedKey);
            System.out.println("File deleted successfully: " + decodedKey);
        } catch (AmazonServiceException e) {
            // AWS Service error (like wrong bucket, permission issue)
            System.err.println("AWS error: " + e.getMessage());
        } catch (SdkClientException e) {
            // Client-side error (like no internet, bad credentials)
            System.err.println("Client error: " + e.getMessage());
        }
    }
}

