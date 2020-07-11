package com.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class ResponseHeader {

    @Getter
    @Setter
    protected String resourceName;

    @Getter
    @Setter
    protected Date timestamp;

    @Getter
    @Setter
    protected ResponseHeaderException error;

    public ResponseHeader() { }

}
