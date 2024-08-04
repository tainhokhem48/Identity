package com.example.SpringBoot.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;



@NoArgsConstructor
@Getter
public enum ErrorCode {

    USER_EXISTED(1001, "User name existed !!!", HttpStatus.BAD_REQUEST),
    ID_NOTFOUND(1002,"id no found!!!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003,"Password >= 8 character", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004, "User not found !!!", HttpStatus.NOT_FOUND),
    LOGIN_NOT_FOUND(1005, "username and password no found!!!", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1006, "you do not at permission", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1007, "unauthenticated", HttpStatus.UNAUTHORIZED),
    UN_VALID_DOB(1008, "invalid day of birth", HttpStatus.UNAUTHORIZED)
    ;


    private  int code ;
    private  String messages;
    private  HttpStatus statusCode;

    ErrorCode(int code, String messages, HttpStatus statusCode) {
        this.code = code;
        this.messages = messages;
        this.statusCode = statusCode;
    }

}
