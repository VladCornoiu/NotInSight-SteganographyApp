package com.example.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
