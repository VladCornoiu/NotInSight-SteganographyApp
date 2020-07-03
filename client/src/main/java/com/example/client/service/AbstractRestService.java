package com.example.client.service;

import com.example.model.*;
import com.example.model.enums.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRestService {

    private static final long serialVersionUID = 1L;

    private final static Logger logger = LoggerFactory.getLogger(AbstractRestService.class);

    @Value("${api.rest.url}")
    protected String apiRestUrl;

    protected final RestTemplate restTemplate;
    protected final ObjectMapper objectMapper;

    protected AbstractRestService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    protected <B extends ResponseBody> Response<B> syncRequest(Request<?> restRequest, HttpMethod method, String resourceName) {
        URI uri = UriComponentsBuilder.fromHttpUrl(apiRestUrl).path(resourceName).build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = mapDataToMultiValueMapBody(restRequest, resourceName);

        try {
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> result = restTemplate.exchange(uri, method, requestEntity, String.class);

            return unmarshall(result.getBody());
        } catch (HttpStatusCodeException | JsonProcessingException e) {
            logger.error("HTTP REST request failed", e);

            return new Response<>(ResponseStatus.ERROR, new DefaultHeader(resourceName, new ResponseHeaderException(e)), null);
        }
    }

    private <B extends ResponseBody> Response<B> unmarshall(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Response<B> response = mapper.readValue(json, Response.class);

        logger.debug("Unmarshalled response=[{}]", response);
        return response;
    }

    abstract LinkedMultiValueMap<String, Object> mapDataToMultiValueMapBody(Request<?> restRequest, String resourceName);
}
