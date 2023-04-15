package com.slinkdigital.fileservice.service.impl;

//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.slinkdigital.fileservice.service.FileService;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//import lombok.AllArgsConstructor;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author TEGA
 */
//@Service
//@AllArgsConstructor
//public class S3Service implements FileService{
//
//    public static final String BUCKET_NAME = "weddingapp";
//    private final AmazonS3Client awsS3Client;
//    
//    @Override
//    public String uploadFile(MultipartFile file, String extension) {
//        var filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
//        var key = UUID.randomUUID().toString() + "." + filenameExtension;
//        var metadata = new ObjectMetadata();
//        metadata.setContentLength(file.getSize());
//        metadata.setContentType(file.getContentType());
//
//        try {
//            awsS3Client.putObject(BUCKET_NAME, key, file.getInputStream(), metadata);
//        } catch (IOException ioException) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
//                    "An Exception occured while uploading the file");
//        }
//
//        awsS3Client.setObjectAcl(BUCKET_NAME, key, CannedAccessControlList.PublicRead);
//
//        return awsS3Client.getResourceUrl(BUCKET_NAME, key);
//    }

//    @Override
//    public Resource downloadFile(String fileName) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    
//}
