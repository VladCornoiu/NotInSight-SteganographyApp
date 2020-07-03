package com.example.model.io;

import com.example.model.RequestBody;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;

@JsonTypeName("GetStegoFileRequest")
@ToString
public class GetStegoFileRequest implements RequestBody {

    @Getter
    @Setter
    private File coverFile;

    @Getter
    @Setter
    private File secretFile;

}
