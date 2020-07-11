package com.example.client.data;

import com.example.client.service.StegoRestApi;
import com.example.client.exception.CouldNotPerformStegoOperationException;
import com.example.model.Response;
import com.example.model.enums.ResponseStatus;
import com.example.model.io.GetSecretFileRequest;
import com.example.model.io.GetSecretFileResponse;
import com.example.model.io.GetStegoFileRequest;
import com.example.model.io.GetStegoFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SteganographyDataModel {

    private static final Logger logger = LoggerFactory.getLogger(SteganographyDataModel.class);

    private StegoRestApi stegoRestApi;

    public SteganographyDataModel(StegoRestApi stegoRestApi) {
        this.stegoRestApi = stegoRestApi;
    }

    public GetStegoFileResponse getStegoFile(String coverFileName, String secretFileName) {

        GetStegoFileRequest request = new GetStegoFileRequest();
        request.setCoverFile(new File("clientside/uploads/coverfile/" + coverFileName));
        request.setSecretFile(new File("clientside/uploads/secretFile/" + secretFileName));

        Response<GetStegoFileResponse> response = stegoRestApi.getStegoFile(request);
        if (response.getStatus() == ResponseStatus.ERROR) {
            String error = new StringBuilder()
                    .append("Failed to create Stego File")
                    .append(response.getHeader().getError().getMessage())
                    .toString();
            throw new CouldNotPerformStegoOperationException(error);
        }
        logger.debug("response {}", response);
        return response.getBody();
    }

    public GetSecretFileResponse getSecretFile(String stegoFileName) {
        GetSecretFileRequest request = new GetSecretFileRequest();
        request.setStegoFile(new File("clientside/uploads/stegofile/" + stegoFileName));

        Response<GetSecretFileResponse> response = stegoRestApi.getSecretFile(request);
        logger.debug("response {}", response);

        return response.getBody();
    }
}