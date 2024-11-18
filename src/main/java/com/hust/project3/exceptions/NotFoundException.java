package com.hust.project3.exceptions;

public class NotFoundException extends BaseException {
    @Override
    public int getStatusCode() {
        return 404;
    }

    public NotFoundException(String message)
    {
        super(message);
    }
}

