package com.ticatag.assassement.air_quality_api.Exceptions;

public class IllegalRequestParamException extends RuntimeException {
    public IllegalRequestParamException(String message) {
        super(message);
    }
}
