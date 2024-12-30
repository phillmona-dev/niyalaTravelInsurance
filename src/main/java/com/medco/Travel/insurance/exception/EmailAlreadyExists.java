package com.medco.Travel.insurance.exception;

public class EmailAlreadyExists extends RuntimeException{

    public EmailAlreadyExists(String message){
        super(message);
    }
}
