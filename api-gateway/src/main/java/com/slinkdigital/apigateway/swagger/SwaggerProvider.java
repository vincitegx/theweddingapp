package com.slinkdigital.apigateway.swagger;

import com.slinkdigital.apigateway.exception.GatewayException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
/**
 *
 * @author TEGA
 */
@Component
@Primary
@Slf4j
public class SwaggerProvider implements SwaggerResourcesProvider{

    public static final String API_URI = "/v2/api-docs";

    private final RouteDefinitionLocator routeLocator;

    public SwaggerProvider(RouteDefinitionLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        try {
            String gatewayPredicate = "/api-gateway";
            List<SwaggerResource> resources = new ArrayList<>();
            routeLocator.getRouteDefinitions().subscribe(routeDefinition -> {
//                log.info("Discovered route definition: {}", routeDefinition);
                String resourceName = routeDefinition.getId();
//            String location = routeDefinition.getPredicates().get(0).getArgs().get("_genkey_0").replace("/**", API_URI);
                String location = routeDefinition.getPredicates().get(0).getArgs().get("pattern").replace("/**", API_URI);
                if (location.contains(gatewayPredicate)) {
                    location = location.replace(gatewayPredicate, "");
                }
//                log.info("Adding swagger resource: {} with location {}", resourceName, location);
                resources.add(swaggerResource(resourceName, location));
            });
            return resources;
        } catch (NullPointerException ex) {
            throw new GatewayException(ex.getMessage());
        }
    }    

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
