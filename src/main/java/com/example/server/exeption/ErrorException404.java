package com.example.server.exeption;

public class ErrorException404 extends RuntimeException{
    public ErrorException404(String message) {
        super(message);
    }
}
