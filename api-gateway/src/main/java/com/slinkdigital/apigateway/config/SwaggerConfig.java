package com.slinkdigital.apigateway.config;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author TEGA
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    Optional<BuildProperties> build;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(true)
                .useDefaultResponseMessages(false)
                .apiInfo(getApiInfo())
                .select()
//                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo getApiInfo() {
        String version;
        if (build.isPresent()) {
            version = build.get().getVersion();
        } else {
            version = "1.0";
        }
        return new ApiInfoBuilder()
                .title("Wedding App API")
                .version(version)
                .description("API-GATEWAY DOCUMENTATION FOR WEDDING APP")
                .contact(new Contact("Slink Digital", "https://slinkdigital.com", "info@slinkdigital.com"))
                .license("Apache 2.0")
                .build();
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder().docExpansion(DocExpansion.LIST).build();
    }

//    @Bean
//    UiConfiguration uiConfig() {
//        return new UiConfiguration("validatorUrl", "list", "alpha", "schema",
//                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
//    }
}
