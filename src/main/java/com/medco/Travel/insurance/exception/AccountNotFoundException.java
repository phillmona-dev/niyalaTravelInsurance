package com.medco.Travel.insurance.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(){
        super("No Account Found With The Credentials Provided!");
    }
}
