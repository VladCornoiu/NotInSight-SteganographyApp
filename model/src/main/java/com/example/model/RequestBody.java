package com.example.model;

import com.example.model.io.GetSecretFileRequest;
import com.example.model.io.GetStegoFileRequest;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GetStegoFileRequest.class, name = "GetStegoFileRequest"),
        @JsonSubTypes.Type(value = GetSecretFileRequest.class, name = "GetSecretFileRequest")
})
public interface RequestBody {

}
