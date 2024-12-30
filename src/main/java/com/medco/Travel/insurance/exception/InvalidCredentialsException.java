package com.medco.Travel.insurance.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String msg)
    {
        super(msg);
    }
}