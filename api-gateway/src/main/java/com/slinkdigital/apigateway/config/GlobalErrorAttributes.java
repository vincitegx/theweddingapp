package com.slinkdigital.apigateway.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;

@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = this.getError(request);
        log.error("Error occured", error);
        Map<String, Object> map = super.getErrorAttributes(request, options);
        map.put("errorDescription", error.getLocalizedMessage());
        map.put("title", "A error has occured");
        map.put("tabTitle", "Error");
        map.put("errorDescriptionTitle", "A error has occured! :(");
        return map;
    }
}
