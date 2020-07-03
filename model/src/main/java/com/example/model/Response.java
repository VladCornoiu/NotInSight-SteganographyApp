package com.example.model;

import com.example.model.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Response<T extends ResponseBody> {

    @Getter
    @Setter
    private ResponseStatus status;

    @Getter
    @Setter
    private ResponseHeader header;

    @Getter
    @Setter
    private T body;

    public Response() { }

    public Response(ResponseStatus status, ResponseHeader header, T body) {
        this.status = status;
        this.header = header;
        this.body = body;
    }
}
