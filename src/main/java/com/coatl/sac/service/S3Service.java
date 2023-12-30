package com.coatl.sac.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Service
//@Log4j2
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3;
    private final S3Template s3Template;


    public byte[] getFile(String fileName, String bucketName) {
        try {
            try (ResponseInputStream<GetObjectResponse> response = s3.getObject(request -> request.bucket(bucketName).key(fileName))) {
                return response.readAllBytes();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("UnusedReturnValue")
    public S3Resource uploadFile(String bucket, String objectKey, InputStream inputStream) {
        // Store object
        return s3Template.upload(bucket, objectKey, inputStream);
    }


    public void deleteFile(String bucket, String objectKey) {
        s3Template.deleteObject(bucket, objectKey);
    }
}
