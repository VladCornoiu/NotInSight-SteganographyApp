package com.example.model;

import com.example.model.io.GetSecretFileResponse;
import com.example.model.io.GetStegoFileResponse;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GetStegoFileResponse.class, name = "GetStegoFileResponse"),
        @JsonSubTypes.Type(value = GetSecretFileResponse.class, name = "GetSecretFileResponse")
})
public interface ResponseBody {

}
