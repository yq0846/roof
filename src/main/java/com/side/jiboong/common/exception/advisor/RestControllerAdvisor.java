package com.side.jiboong.common.exception.advisor;

import com.side.jiboong.common.exception.*;
import com.side.jiboong.presentation.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(basePackages = "com.side.jiboong")
public class RestControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(UnauthorizedException e) {
        return createResponseEntity(e, HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException e) {
        return createResponseEntity(e, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(AuthenticationException e) {
        log.error(e.getMessage());
        return createResponseEntity(e, HttpStatus.UNAUTHORIZED, "Bad credentials");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        return createResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
        return createResponseEntity(e, HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        return createResponseEntity(e, HttpStatus.BAD_REQUEST, "Bad request");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException e) {
        return createResponseEntity(e, HttpStatus.UNAUTHORIZED, "Bad credentials");
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<ApiResponse<Void>> handleMailSendException(MailSendException e) {
        return createResponseEntity(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException e) {
        return createResponseEntity(e, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return createResponseEntity(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidVerificationCodeException(InvalidVerificationCodeException e) {
        return createResponseEntity(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InvalidResetCodeException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidResetCodeException(InvalidResetCodeException e) {
        return createResponseEntity(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AuthArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthArgumentException(AuthArgumentException e) {
        return createResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ApiResponse<Void>> handleMailException(MailException e) {
        return createResponseEntity(e, HttpStatus.BAD_REQUEST, "MAIL " + e.getMessage());
    }

    private <T> ResponseEntity<T> createResponseEntity(Exception e, HttpStatusCode statusCode, String message) {
        @SuppressWarnings("unchecked")
        T response = (T) ApiResponse.error(message);

        return ResponseEntity.status(statusCode).body(response);
    }
}
