package com.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Request<T extends RequestBody> {

    @Getter
    @Setter
    private T data;

    public Request() { }

    public Request(T data) {
        this.data = data;
    }
}
