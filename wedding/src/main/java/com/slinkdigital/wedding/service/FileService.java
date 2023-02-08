package com.slinkdigital.wedding.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
public interface FileService {
    
    String uploadFile(MultipartFile file);
    
    List<String> uploadFiles(List<MultipartFile> files);
}
