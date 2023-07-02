package com.slinkdigital.vendor.service.impl;

import com.slinkdigital.vendor.exception.VendorException;
import com.slinkdigital.vendor.service.FileService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{

    private final HttpServletRequest request;
    private final WebClient.Builder webClientBuilder;
    
    @Override
    public String uploadFile(MultipartFile file) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            String fileExt = StringUtils.getFilenameExtension(file.getOriginalFilename());
            builder.part("file", new ByteArrayResource(file.getBytes())).filename(file.getName());
            return webClientBuilder.build().post()
                    .uri("http://API-GATEWAY/api/fs/v1/files",
                            uriBuilder -> uriBuilder
                                    .queryParam("file", file)
                                    .queryParam("ext", fileExt)
                                    .build())
                    .header("authorization", request.getHeader(HttpHeaders.AUTHORIZATION))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA.toString())
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (IOException ex) {
            throw new VendorException(ex.getMessage());
        }
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();
        files.forEach(f->imageUrls.add(uploadFile(f)));
        return imageUrls;
    }
    
}
