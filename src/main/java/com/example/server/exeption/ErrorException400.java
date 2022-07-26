package com.example.server.exeption;

public class ErrorException400 extends RuntimeException{
    public ErrorException400(String message) {
        super(message);
    }
}
