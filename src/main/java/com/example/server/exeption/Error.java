package com.example.server.exeption;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Error {
    private int code;
    private String message;
}
