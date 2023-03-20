package com.slinkdigital.user.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author TEGA
 */
@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(getApiInfo())
                .externalDocs(new ExternalDocumentation()
                        .description("Wedding API Documentation")
                        .url("https://github.com/vincitegx/theweddingapp"));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Autowired
    Optional<BuildProperties> build;

    private Info getApiInfo() {
        String version;
        if (build.isPresent()) {
            version = build.get().getVersion();
        } else {
            version = "1.0";
        }
        Contact c = new Contact();
        c.setName("David Tega");
        c.setEmail("davidogbodu3056@gmail.com");
        c.setUrl("https://weddingapp.com");
        return new Info()
                .title("Wedding App API")
                .version(version)
                .description("USER DOCUMENTATION FOR WEDDING APP")
                .contact(c)
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));
    }
}
