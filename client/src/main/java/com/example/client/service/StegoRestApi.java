package com.example.client.service;

import com.example.model.Response;
import com.example.model.io.GetSecretFileRequest;
import com.example.model.io.GetSecretFileResponse;
import com.example.model.io.GetStegoFileResponse;
import com.example.model.io.GetStegoFileRequest;

public interface StegoRestApi {

    Response<GetStegoFileResponse> getStegoFile(GetStegoFileRequest request);

    Response<GetSecretFileResponse> getSecretFile(GetSecretFileRequest request);
}
