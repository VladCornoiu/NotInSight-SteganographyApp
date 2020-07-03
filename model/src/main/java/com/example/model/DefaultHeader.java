package com.example.model;

import lombok.ToString;

import java.util.Date;

@ToString
public class DefaultHeader extends ResponseHeader {

    public DefaultHeader(String resourceName, ResponseHeaderException error) {
        this.resourceName = resourceName;
        this.error = error;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    @Override
    public Date getTimestamp() {
        return new Date();
    }

    @Override
    public ResponseHeaderException getError() {
        return error;
    }
}
