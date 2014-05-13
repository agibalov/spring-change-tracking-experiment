package me.loki2302.client.api;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class OperationsTemplate {
    protected final RestTemplate restTemplate;

    protected OperationsTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected static String buildUri(String path, Object... vars) {
        return UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .path(path)
                .buildAndExpand(vars)
                .toUriString();
    }
}
