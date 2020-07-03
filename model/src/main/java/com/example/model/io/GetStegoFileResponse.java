package com.example.model.io;

import com.example.model.ResponseBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class GetStegoFileResponse implements ResponseBody {

    @Getter
    @Setter
    private String fileName;

    @Getter
    @Setter
    private byte[] data;

}
