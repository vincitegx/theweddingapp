package com.slinkdigital.fileservice.controller;

import com.slinkdigital.fileservice.service.FileService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/fu/v1/files")
@RequiredArgsConstructor
public class FileController1 {
    
    private final FileService fileService;
    
    @GetMapping("{fileName:.+}")
    public ResponseEntity<Resource> displayFile(@PathVariable String fileName, HttpServletRequest request){
        Resource resource = fileService.downloadFile(fileName);
        String mimeType;
        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            mimeType = MediaType.ALL_VALUE;
        }
        if(mimeType == null) {
            mimeType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+resource.getFilename()+"\"")     
                .body(resource);
    }    
}
