package com.example.SpringBoot.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class ErrorCodeException extends RuntimeException{
    private ErrorCode errorCode;

    public ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.getMessages());
        this.errorCode = errorCode;
    }
}
