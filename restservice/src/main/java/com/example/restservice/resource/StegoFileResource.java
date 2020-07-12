package com.example.restservice.resource;

import com.example.model.DefaultHeader;
import com.example.model.Response;
import com.example.model.ResponseHeaderException;
import com.example.model.enums.ResponseStatus;
import com.example.model.io.GetSecretFileResponse;
import com.example.model.io.GetStegoFileResponse;
import com.example.restservice.model.SecretData;
import com.example.restservice.service.ServerStegoFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class StegoFileResource {

    private final static Logger logger = LoggerFactory.getLogger(StegoFileResource.class);

    @Autowired
    ServerStegoFileService serverStegoFileService;

    @RequestMapping(
            value = "api/performStego",
            method = RequestMethod.POST
    )
    public Response<GetStegoFileResponse> performStego(@RequestParam("coverfile") MultipartFile coverFile,
                                                       @RequestParam("secretfile") MultipartFile secretFile) {
        try {
            byte[] result = serverStegoFileService.computeStegoFile(coverFile, secretFile);

            GetStegoFileResponse body = new GetStegoFileResponse();
            body.setData(result);
            body.setFileName(coverFile.getOriginalFilename().substring(0, coverFile.getOriginalFilename().lastIndexOf(".")) + ".jpg");
            return new Response<>(ResponseStatus.SUCCESS, new DefaultHeader("api/performStego", null), body);

        } catch (Exception ex) {
            logger.error("The Stego process failed with error: " + ex.getStackTrace().toString());
            return new Response<>(ResponseStatus.ERROR, new DefaultHeader("api/performStego", new ResponseHeaderException(ex)), null);
        }
    }

    @RequestMapping(
            value = "api/retrieveSecret",
            method = RequestMethod.POST
    )
    public Response<GetSecretFileResponse> retrieveSecret(@RequestParam("stegofile") MultipartFile stegoFile) {
        try {
            SecretData secretData = serverStegoFileService.retrieveSecret(stegoFile);

            GetSecretFileResponse body = new GetSecretFileResponse();
            body.setData(secretData.getData());
            body.setFileName(secretData.getFilename());

            return new Response<>(ResponseStatus.SUCCESS, new DefaultHeader("api/retrieveSecret", null), body);

        } catch (Exception ex) {
            logger.error("The retrieval of secret data process failed with error: " + ex.getStackTrace());
            return new Response<>(ResponseStatus.ERROR, new DefaultHeader("api/retrieveSecret", new ResponseHeaderException(ex)), null);
        }
    }
}
