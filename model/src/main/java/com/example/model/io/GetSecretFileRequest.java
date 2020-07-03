package com.example.model.io;

import com.example.model.RequestBody;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;

@JsonTypeName("GetSecretFileRequest")
@ToString
public class GetSecretFileRequest implements RequestBody {

    @Getter
    @Setter
    private File stegoFile;
}
