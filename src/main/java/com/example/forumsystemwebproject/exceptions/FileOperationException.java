package com.example.forumsystemwebproject.exceptions;

import java.io.File;

public class FileOperationException extends RuntimeException{

    public FileOperationException(String message) {
        super(message);
    }
}
