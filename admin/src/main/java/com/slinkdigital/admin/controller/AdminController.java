package com.slinkdigital.admin.controller;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    
    @GetMapping("/test/ok")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("test success");
    }
    
}
