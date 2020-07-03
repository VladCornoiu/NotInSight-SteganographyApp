package com.example.client.service;

import com.example.model.Request;
import com.example.model.Response;
import com.example.model.io.GetSecretFileRequest;
import com.example.model.io.GetSecretFileResponse;
import com.example.model.io.GetStegoFileResponse;
import com.example.model.io.GetStegoFileRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

@Service
public class StegoRestService extends AbstractRestService implements StegoRestApi {

    private static final Logger logger = LoggerFactory.getLogger(StegoRestService.class);

    @Override
    public Response<GetStegoFileResponse> getStegoFile(GetStegoFileRequest request) {
        Request<GetStegoFileRequest> restRequest = new Request<>(request);
        return syncRequest(restRequest, HttpMethod.POST, "/performStego");
    }

    @Override
    public Response<GetSecretFileResponse> getSecretFile(GetSecretFileRequest request) {
        Request<GetSecretFileRequest> restRequest = new Request<>(request);
        return syncRequest(restRequest, HttpMethod.POST, "/retrieveSecret");
    }

    protected LinkedMultiValueMap<String, Object> mapDataToMultiValueMapBody(Request<?> restRequest, String resourceName) {
        if ("/performStego".equals(resourceName)) {
            Request<GetStegoFileRequest> stegoRequest = (Request<GetStegoFileRequest>) restRequest;

            LinkedMultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("coverfile", new FileSystemResource(stegoRequest.getData().getCoverFile()));
            requestBody.add("secretfile", new FileSystemResource(stegoRequest.getData().getSecretFile()));

            return requestBody;
        } else if ("/retrieveSecret".equals(resourceName)) {
            Request<GetSecretFileRequest> stegoRequest = (Request<GetSecretFileRequest>) restRequest;

            LinkedMultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("stegofile", new FileSystemResource(stegoRequest.getData().getStegoFile()));

            return requestBody;
        } else {
            logger.error("Endpoint not defined");
            return null;
        }
    }
}
