package com.slinkdigital.vendor.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author TEGA
 */
@Component
@Data
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    
    @NotNull
    private String url;
    
    @NotNull
    private String gatewayUrl;
    
}
