package com.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.glassfish.jersey.internal.util.ExceptionUtils;

import javax.xml.bind.annotation.XmlElement;

@ToString
public class ResponseHeaderException {

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String cause;

    @Getter
    @Setter
    private String stackTrace;

    public ResponseHeaderException() { }

    public ResponseHeaderException(Throwable exception) {
        this.message = exception.getMessage();
        this.type = exception.getClass().getSimpleName();
        this.cause = (exception.getCause() != null) ? exception.getCause().getMessage() : "Unknown cause";
        this.stackTrace = ExceptionUtils.exceptionStackTraceAsString(exception);
    }
}
