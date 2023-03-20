package com.slinkdigital.apigateway.config;

import com.slinkdigital.apigateway.exception.GatewayException;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author TEGA
 */
@Configuration
@Slf4j
public class SwaggerConfig {

    private static final String API_URI = "/v3/api-docs";

    private final RouteDefinitionLocator locator;

    public SwaggerConfig(RouteDefinitionLocator locator) {
        this.locator = locator;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            name = {"springdoc.use-management-port"},
            havingValue = "false",
            matchIfMissing = true
    )
    public GroupedOpenApi apis(SwaggerUiConfigProperties swaggerUiConfigProperties) {
        try {
            Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
            locator.getRouteDefinitions().subscribe(routeDefinition -> {
                log.info("Discovered route definition: {}", routeDefinition);
                String resourceName = routeDefinition.getId();
//                String location = routeDefinition.getPredicates().get(0).getArgs().get("_genkey_0").replace("/**", API_URI);
                String location = routeDefinition.getPredicates().get(0).getArgs().get("pattern").replace("/**", API_URI);
                log.info("Adding swagger resource: {} with location {}", resourceName, location);
                urls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl(resourceName, location, resourceName));
            });
            swaggerUiConfigProperties.setUrls(urls);
            return GroupedOpenApi.builder()
                    .group("resource")
                    .pathsToMatch("/api/**")
                    .build();
        } catch (NullPointerException ex) {
            log.info(ex.getLocalizedMessage());
            throw new GatewayException(ex.getMessage());
        }
    }

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
                .description("API-GATEWAY DOCUMENTATION FOR WEDDING APP")
                .contact(c)
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
