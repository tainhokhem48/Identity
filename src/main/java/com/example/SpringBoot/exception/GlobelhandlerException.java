package com.example.SpringBoot.exception;

import com.example.SpringBoot.dto.request.ApiResponse;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobelhandlerException {
    private static final String MIN_ATTRIBUTES = "min";
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlerRuntimeException(RuntimeException exception){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(1001);
        apiResponse.setMessages(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(value = JwtException.class)
    ResponseEntity<ApiResponse> handlerJwtException(JwtException exception){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(1001);
        apiResponse.setMessages(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> HandlerMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        ApiResponse apiResponse = new ApiResponse<>();
        String Enumkey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.USER_EXISTED;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(Enumkey);
            var contrainViolation = exception.getBindingResult().getAllErrors()
                    .getFirst().unwrap(ConstraintViolation.class);
            attributes = contrainViolation.getConstraintDescriptor().getAttributes();
            log.info(attributes.toString());
        }catch (IllegalArgumentException e){

        }

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessages(Objects.isNull(attributes) ?
                MapAttribute(errorCode.getMessages(),attributes)
                : errorCode.getMessages());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String MapAttribute(String message, Map<String, Object> attributes){
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTES));
        return message.replace("{" + MIN_ATTRIBUTES + "}", minValue);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlerAccessDeniedException(AccessDeniedException exception){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .messages(errorCode.getMessages())
                        .build()
        );
    }
    @ExceptionHandler(value = ErrorCodeException.class)
    ResponseEntity<ApiResponse> handlerErrorCodeException(ErrorCodeException exception){
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessages(errorCode.getMessages());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }
}
