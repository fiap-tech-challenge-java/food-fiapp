package com.fiap.foodfiapp.infrastructure.rest.exception;

import com.fiap.foodfiapp.core.domain.exception.*;
import com.fiap.foodfiapp.infrastructure.exception.InfrastructureException;
import com.fiap.foodfiapp.infrastructure.exception.ResourceNotFoundException;
import com.fiap.foodfiapp.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex,
                                                                     HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "USER_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                request.getRequestURI()
        );
        logger.warn("User not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UserTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserTypeNotFoundException(UserTypeNotFoundException ex,
                                                                         HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "USER_TYPE_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                request.getRequestURI()
        );
        logger.warn("UserType not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAddressNotFoundException(AddressNotFoundException ex,
                                                                        HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "ADDRESS_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                request.getRequestURI()
        );
        logger.warn("Address not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex,
                                                                           HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "EMAIL_ALREADY_EXISTS",
                HttpStatus.CONFLICT,
                request.getRequestURI()
        );
        logger.warn("Email already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(LoginAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleLoginAlreadyExistsException(LoginAlreadyExistsException ex,
                                                                           HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "LOGIN_ALREADY_EXISTS",
                HttpStatus.CONFLICT,
                request.getRequestURI()
        );
        logger.warn("Login already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(CpfAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCpfAlreadyExistsException(CpfAlreadyExistsException ex,
                                                                         HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "CPF_ALREADY_EXISTS",
                HttpStatus.CONFLICT,
                request.getRequestURI()
        );
        logger.warn("CPF already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEmailException(InvalidEmailException ex,
                                                                     HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "INVALID_EMAIL",
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        logger.warn("Invalid email: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidCpfException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCpfException(InvalidCpfException ex,
                                                                   HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "INVALID_CPF",
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        logger.warn("Invalid CPF: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException ex,
                                                                        HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "INVALID_PASSWORD",
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        logger.warn("Invalid password: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataException(InvalidDataException ex,
                                                                    HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "INVALID_DATA",
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        logger.warn("Invalid data: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorageException(FileStorageException ex,
                                                                    HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "FILE_STORAGE_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );
        logger.error("File storage error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex,
                                                                           HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "UNAUTHORIZED_ACCESS",
                HttpStatus.FORBIDDEN,
                request.getRequestURI()
        );
        logger.warn("Unauthorized access: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex,
                                                                 HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "BUSINESS_RULE_VIOLATION",
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        logger.warn("Business rule violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                         HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "RESOURCE_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                request.getRequestURI()
        );
        logger.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponse> handleInfrastructureException(InfrastructureException ex,
                                                                       HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                "An infrastructure error occurred. Please try again later.",
                "INFRASTRUCTURE_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );
        logger.error("Infrastructure error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                    HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = createErrorResponse(
                "Data validation error",
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        errorResponse.setFieldErrors(errors);

        logger.warn("Validation error: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex,
                                                                            HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "CONSTRAINT_VIOLATION",
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        logger.warn("Constraint violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                                                                               HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                "Invalid request. Please check the format of the submitted data",
                "INVALID_REQUEST_BODY",
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        logger.warn("Error reading the request body: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format("The parameter '%s' must be of type %s", ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        ErrorResponse errorResponse = createErrorResponse(
                message,
                "INVALID_PARAMETER_TYPE",
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        logger.warn("Invalid parameter type: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        String message = String.format("Required parameter '%s' was not provided", ex.getParameterName());

        ErrorResponse errorResponse = createErrorResponse(
                message,
                "MISSING_PARAMETER",
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        logger.warn("Required parameter missing: {}", ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String message = String.format("HTTP method '%s' is not supported for this endpoint", ex.getMethod());

        ErrorResponse errorResponse = createErrorResponse(
                message,
                "METHOD_NOT_ALLOWED",
                HttpStatus.METHOD_NOT_ALLOWED,
                request.getRequestURI()
        );
        logger.warn("HTTP method not supported: {} {}", ex.getMethod(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                       HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                "Endpoint not found",
                "ENDPOINT_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                request.getRequestURI()
        );
        logger.warn("Endpoint not found: {} {}", ex.getHttpMethod(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
                                                                               HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                "Data integrity error. Check if the data does not violate database constraints",
                "DATA_INTEGRITY_VIOLATION",
                HttpStatus.CONFLICT,
                request.getRequestURI()
        );
        logger.error("Data integrity error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex,
                                                                       HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                "Authentication failed: " + ex.getMessage(),
                "AUTHENTICATION_FAILED",
                HttpStatus.UNAUTHORIZED,
                request.getRequestURI()
        );
        logger.warn("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex,
                                                                       HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                "Invalid credentials",
                "INVALID_CREDENTIALS",
                HttpStatus.UNAUTHORIZED,
                request.getRequestURI()
        );
        logger.warn("Invalid credentials for {}", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
                                                                     HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                "Access denied. You do not have permission to access this resource",
                "ACCESS_DENIED",
                HttpStatus.FORBIDDEN,
                request.getRequestURI()
        );
        logger.warn("Access denied for {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                "Internal server error. Please try again later",
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );
        logger.error("Unhandled internal error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(UserTypeNameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserTypeNameAlreadyExistsException(UserTypeNameAlreadyExistsException ex,
                                                                                  HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "USER_TYPE_NAME_ALREADY_EXISTS",
                HttpStatus.CONFLICT,
                request.getRequestURI()
        );
        logger.warn("UserType name already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(UserTypeInUseException.class)
    public ResponseEntity<ErrorResponse> handleUserTypeInUseException(UserTypeInUseException ex,
                                                                      HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "USER_TYPE_IN_USE",
                HttpStatus.CONFLICT,
                request.getRequestURI()
        );
        logger.warn("UserType in use: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(CoreUserTypeModificationException.class)
    public ResponseEntity<ErrorResponse> handleCoreUserTypeModificationException(CoreUserTypeModificationException ex,
                                                                                   HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ex.getMessage(),
                "CORE_USER_TYPE_MODIFICATION_NOT_ALLOWED",
                HttpStatus.CONFLICT,
                request.getRequestURI()
        );
        logger.warn("Attempt to modify or delete a core user type: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    private ErrorResponse createErrorResponse(String message, String code, HttpStatus status, String path) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        errorResponse.setCode(code);
        errorResponse.setStatus(status.value());
        errorResponse.setPath(path);
        errorResponse.setTimestamp(OffsetDateTime.now());
        return errorResponse;
    }
}
