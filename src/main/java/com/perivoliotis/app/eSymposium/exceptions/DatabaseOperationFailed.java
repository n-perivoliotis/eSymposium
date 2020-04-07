package com.perivoliotis.app.eSymposium.exceptions;

public class DatabaseOperationFailed extends RuntimeException {

    public DatabaseOperationFailed(String message) {
        super(message);
    }
}
