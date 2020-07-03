package com.example.restservice.model;

import lombok.Getter;
import lombok.Setter;

public class SecretData {

    @Getter
    @Setter
    private String filename;

    @Getter
    @Setter
    private byte[] data;

    public SecretData(String filename, byte[] data) {
        this.filename = filename;
        this.data = data;
    }
}
