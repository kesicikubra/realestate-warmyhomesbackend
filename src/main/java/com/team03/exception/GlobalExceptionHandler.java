package com.team03.exception;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import com.team03.payload.response.business.ResponseMessage;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ResponseMessage<String>> handleAllException(
          Exception ex, WebRequest request) {
    ResponseMessage<String> responseMessage = ResponseMessage.<String>builder()
            .message(ex.getMessage())
            //mesaji degistir
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ResponseMessage<Object>> handleMethodArgumentNotValidEx(
          MethodArgumentNotValidException ex, WebRequest request) {
    Map<String, Object> errors = getErrorMap(ex);
    ResponseMessage<Object> responseMessage = ResponseMessage.<Object>builder()
            .object(errors)
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public final ResponseEntity<ResponseMessage<Object>> handleConstraintViolationEx(
          MethodArgumentNotValidException ex, WebRequest request) {
    Map<String, Object> errors = getErrorMap(ex);
    ResponseMessage<Object> responseMessage = ResponseMessage.<Object>builder()
            .object(errors)
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
  }
  @ExceptionHandler(ConflictException.class)
  public final ResponseEntity<ResponseMessage<String>> handleConflictException(
          ConflictException ex, WebRequest request) {
    ResponseMessage<String> responseMessage = ResponseMessage.<String>builder()
            .message(ex.getMessage())
            .httpStatus(HttpStatus.CONFLICT)
            .build();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMessage);
  }

  @ExceptionHandler(BadRequestException.class)
  public final ResponseEntity<ResponseMessage<String>> handleBadRequestException(
          BadRequestException ex, WebRequest request) {
    ResponseMessage<String> responseMessage = ResponseMessage.<String>builder()
            .message(ex.getMessage())
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
  }


  @ExceptionHandler(ResourceNotFoundException.class)
  public final ResponseEntity<ResponseMessage<String>> handleResourceNotFoundException(
          ResourceNotFoundException ex, WebRequest request) {
    ResponseMessage<String> responseMessage = ResponseMessage.<String>builder()
            .message(ex.getMessage())
            .httpStatus(HttpStatus.NOT_FOUND)
            .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
  }
  private Map<String, Object> getErrorMap(MethodArgumentNotValidException ex) {
    Map<String, Object> errors = new HashMap<>();
    ex.getBindingResult()
            .getAllErrors()
            .forEach(
                    x -> {
                      String errorMessage = x.getDefaultMessage();
                      if (x instanceof FieldError) {
                        String fieldName = ((FieldError) x).getField();
                        errors.put(fieldName, errorMessage);
                      } else {
                        String objectName = x.getObjectName();
                        errors.put(objectName, errorMessage);
                      }
                    });
    return errors;
  }
}
