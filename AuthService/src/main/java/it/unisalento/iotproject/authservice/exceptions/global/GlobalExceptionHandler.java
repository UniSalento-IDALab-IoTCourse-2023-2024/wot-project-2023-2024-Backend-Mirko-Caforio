package it.unisalento.iotproject.authservice.exceptions.global;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomErrorException.class)
    protected ResponseEntity<CustomErrorResponse> handleTransactionNotFoundException(RuntimeException ex) {
        CustomErrorException exception = (CustomErrorException) ex;
        return ResponseEntity.status(exception.getErrorResponse().getStatus()).body(exception.getErrorResponse());
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<CustomErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .traceId(UUID.randomUUID().toString())
                .timestamp(OffsetDateTime.now().toString())
                .status(HttpStatus.FORBIDDEN)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .traceId(UUID.randomUUID().toString())
                .timestamp(OffsetDateTime.now().toString())
                .status(HttpStatus.resolve(statusCode.value()))
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }
}
