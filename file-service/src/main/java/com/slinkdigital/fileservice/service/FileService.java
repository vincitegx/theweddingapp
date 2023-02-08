package com.slinkdigital.fileservice.service;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
public interface FileService {
    String uploadFile(MultipartFile file, String extension);
    List<String> uploadFiles(List<MultipartFile> files, List<String> extensions);
    Resource downloadFile(String fileName);
}
