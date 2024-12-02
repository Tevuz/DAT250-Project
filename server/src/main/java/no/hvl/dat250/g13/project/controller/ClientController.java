package no.hvl.dat250.g13.project.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ClientController {

    Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Value("classpath:${resources.client.config}")
    private Resource clientConfig;

    @Value("${resources.client.index}")
    private String indexPath;

    private final RequestMappingHandlerMapping mapper;

    public ClientController(RequestMappingHandlerMapping mapper) {
        this.mapper = mapper;

    }

    @PostConstruct
    public void registerEndpoints() throws NoSuchMethodException, IOException {
        var endpoints = new ArrayList<String>();
        try (InputStream inputStream = clientConfig.getInputStream()) {
            JsonNode rootNode = new ObjectMapper().readTree(inputStream);
            JsonNode pathsNode = rootNode.at("/client/paths");
            pathsNode.fieldNames().forEachRemaining(endpoints::add);
        }
        logger.info("📌 Client paths: {}", endpoints);
        Method method = getClass().getMethod("getClient");

        for (String endpoint : endpoints) {
            mapper.registerMapping(RequestMappingInfo.paths(endpoint).methods(RequestMethod.GET).build(), this, method);
        }
    }

    public ResponseEntity<Resource> getClient() {
        return ResponseEntity.ok().body(new ClassPathResource(indexPath));
    }


}
