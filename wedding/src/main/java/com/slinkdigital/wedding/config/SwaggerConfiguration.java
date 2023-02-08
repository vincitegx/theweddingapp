package com.slinkdigital.wedding.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author TEGA
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${organization.properties.name}")
    private String organizationName;

    @Value("${organization.properties.mail}")
    private String organizationMail;

    @Value("${organization.properties.website.url}")
    private String organizationUrl;

    @Value("${project.api.title}")
    private String projectApiTitle;

    @Value("${project.api.version}")
    private String projectApiVersion;

    @Value("${project.api.description}")
    private String projectApiDescription;

    @Value("${project.license}")
    private String projectLicense;

    @Bean
    public Docket weddingAppApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title(projectApiTitle)
                .version(projectApiVersion)
                .description(projectApiDescription)
                .contact(new Contact(organizationName, organizationUrl, organizationMail))
                .license(projectLicense)
                .build();
    }

}
