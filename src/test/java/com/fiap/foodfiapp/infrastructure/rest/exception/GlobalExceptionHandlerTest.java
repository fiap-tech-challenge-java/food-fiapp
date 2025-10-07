package com.fiap.foodfiapp.infrastructure.rest.exception;

import com.fiap.foodfiapp.core.domain.exception.*;
import com.fiap.foodfiapp.infrastructure.exception.InfrastructureException;
import com.fiap.foodfiapp.infrastructure.exception.ResourceNotFoundException;
import com.fiap.foodfiapp.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Global Exception Handler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    @DisplayName("Should handle UserNotFoundException with NOT_FOUND status")
    void shouldHandleUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException("id", "123");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserNotFoundException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getCode()).isEqualTo("USER_NOT_FOUND");
        assertThat(response.getBody().getMessage()).contains("User not found with id: '123'");
        assertThat(response.getBody().getPath()).isEqualTo("/api/test");
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle UserTypeNotFoundException with NOT_FOUND status")
    void shouldHandleUserTypeNotFoundException() {
        UserTypeNotFoundException exception = new UserTypeNotFoundException("name", "ADMIN");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserTypeNotFoundException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getCode()).isEqualTo("USER_TYPE_NOT_FOUND");
        assertThat(response.getBody().getMessage()).contains("UserType not found with name: 'ADMIN'");
    }

    @Test
    @DisplayName("Should handle AddressNotFoundException with NOT_FOUND status")
    void shouldHandleAddressNotFoundException() {
        AddressNotFoundException exception = new AddressNotFoundException("id", "456");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAddressNotFoundException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getCode()).isEqualTo("ADDRESS_NOT_FOUND");
        assertThat(response.getBody().getMessage()).contains("Address not found with id: '456'");
    }

    @Test
    @DisplayName("Should handle EmailAlreadyExistsException with CONFLICT status")
    void shouldHandleEmailAlreadyExistsException() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException("test@test.com");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleEmailAlreadyExistsException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(409);
        assertThat(response.getBody().getCode()).isEqualTo("EMAIL_ALREADY_EXISTS");
        assertThat(response.getBody().getMessage()).contains("Email 'test@test.com' is already registered");
    }

    @Test
    @DisplayName("Should handle LoginAlreadyExistsException with CONFLICT status")
    void shouldHandleLoginAlreadyExistsException() {
        LoginAlreadyExistsException exception = new LoginAlreadyExistsException("testuser");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleLoginAlreadyExistsException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(409);
        assertThat(response.getBody().getCode()).isEqualTo("LOGIN_ALREADY_EXISTS");
        assertThat(response.getBody().getMessage()).contains("Login 'testuser' is already registered");
    }

    @Test
    @DisplayName("Should handle InvalidEmailException with BAD_REQUEST status")
    void shouldHandleInvalidEmailException() {
        InvalidEmailException exception = new InvalidEmailException("Invalid email format");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidEmailException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_EMAIL");
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid email format");
    }

    @Test
    @DisplayName("Should handle InvalidCpfException with BAD_REQUEST status")
    void shouldHandleInvalidCpfException() {
        InvalidCpfException exception = new InvalidCpfException();

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidCpfException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_CPF");
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid CPF format");
    }

    @Test
    @DisplayName("Should handle InvalidPasswordException with BAD_REQUEST status")
    void shouldHandleInvalidPasswordException() {
        InvalidPasswordException exception = new InvalidPasswordException("Password must have at least 8 characters");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidPasswordException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_PASSWORD");
        assertThat(response.getBody().getMessage()).isEqualTo("Password must have at least 8 characters");
    }

    @Test
    @DisplayName("Should handle InvalidDataException with BAD_REQUEST status")
    void shouldHandleInvalidDataException() {
        InvalidDataException exception = new InvalidDataException("age", "invalid");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidDataException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_DATA");
        assertThat(response.getBody().getMessage()).contains("Invalid data for field 'age': invalid");
    }

    @Test
    @DisplayName("Should handle UnauthorizedAccessException with FORBIDDEN status")
    void shouldHandleUnauthorizedAccessException() {
        UnauthorizedAccessException exception = new UnauthorizedAccessException("Access denied for this resource");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUnauthorizedAccessException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(403);
        assertThat(response.getBody().getCode()).isEqualTo("ACCESS_DENIED");
        assertThat(response.getBody().getMessage()).isEqualTo("Access denied for this resource");
    }

    @Test
    @DisplayName("Should handle BusinessException with BAD_REQUEST status")
    void shouldHandleBusinessException() {
        BusinessException exception = new BusinessException("Business rule violation");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getCode()).isEqualTo("BUSINESS_RULE_VIOLATION");
        assertThat(response.getBody().getMessage()).isEqualTo("Business rule violation");
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException with NOT_FOUND status")
    void shouldHandleResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFoundException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getCode()).isEqualTo("RESOURCE_NOT_FOUND");
        assertThat(response.getBody().getMessage()).isEqualTo("Resource not found");
    }

    @Test
    @DisplayName("Should handle InfrastructureException with INTERNAL_SERVER_ERROR status")
    void shouldHandleInfrastructureException() {
        InfrastructureException exception = new InfrastructureException("Database connection failed");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInfrastructureException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getCode()).isEqualTo("INFRASTRUCTURE_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("An infrastructure error occurred. Please try again later.");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with validation errors")
    void shouldHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("user", "name", "Name is required");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("Data validation error");
        assertThat(response.getBody().getFieldErrors()).containsEntry("name", "Name is required");
    }

    @Test
    @DisplayName("Should handle ConstraintViolationException with BAD_REQUEST status")
    void shouldHandleConstraintViolationException() {
        Set<ConstraintViolation<?>> violations = Collections.emptySet();
        ConstraintViolationException exception = new ConstraintViolationException("Validation failed", violations);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConstraintViolationException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getCode()).isEqualTo("CONSTRAINT_VIOLATION");
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException with BAD_REQUEST status")
    void shouldHandleHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        when(exception.getMessage()).thenReturn("JSON parse error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleHttpMessageNotReadableException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_REQUEST_BODY");
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid request. Please check the format of the submitted data");
    }

    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException with BAD_REQUEST status")
    void shouldHandleMethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("id");
        when(exception.getRequiredType()).thenReturn((Class) Long.class);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMethodArgumentTypeMismatchException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_PARAMETER_TYPE");
        assertThat(response.getBody().getMessage()).contains("The parameter 'id' must be of type Long");
    }

    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException with null required type")
    void shouldHandleMethodArgumentTypeMismatchExceptionWithNullType() {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("id");
        when(exception.getRequiredType()).thenReturn(null);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMethodArgumentTypeMismatchException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("The parameter 'id' must be of type unknown");
    }

    @Test
    @DisplayName("Should handle MissingServletRequestParameterException with BAD_REQUEST status")
    void shouldHandleMissingServletRequestParameterException() {
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("userId", "String");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMissingServletRequestParameterException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getCode()).isEqualTo("MISSING_PARAMETER");
        assertThat(response.getBody().getMessage()).contains("Required parameter 'userId' was not provided");
    }

    @Test
    @DisplayName("Should handle HttpRequestMethodNotSupportedException with METHOD_NOT_ALLOWED status")
    void shouldHandleHttpRequestMethodNotSupportedException() {
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("POST");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleHttpRequestMethodNotSupportedException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(405);
        assertThat(response.getBody().getCode()).isEqualTo("METHOD_NOT_ALLOWED");
        assertThat(response.getBody().getMessage()).contains("HTTP method 'POST' is not supported for this endpoint");
    }

    @Test
    @DisplayName("Should handle NoHandlerFoundException with NOT_FOUND status")
    void shouldHandleNoHandlerFoundException() {
        NoHandlerFoundException exception = new NoHandlerFoundException("GET", "/unknown", null);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNoHandlerFoundException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getCode()).isEqualTo("ENDPOINT_NOT_FOUND");
        assertThat(response.getBody().getMessage()).isEqualTo("Endpoint not found");
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException with CONFLICT status")
    void shouldHandleDataIntegrityViolationException() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Constraint violation");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDataIntegrityViolationException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(409);
        assertThat(response.getBody().getCode()).isEqualTo("DATA_INTEGRITY_VIOLATION");
        assertThat(response.getBody().getMessage()).isEqualTo("Data integrity error. Check if the data does not violate database constraints");
    }

    @Test
    @DisplayName("Should handle AuthenticationException with UNAUTHORIZED status")
    void shouldHandleAuthenticationException() {
        AuthenticationException exception = mock(AuthenticationException.class);
        when(exception.getMessage()).thenReturn("Authentication failed");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAuthenticationException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(401);
        assertThat(response.getBody().getCode()).isEqualTo("AUTHENTICATION_FAILED");
        assertThat(response.getBody().getMessage()).isEqualTo("Authentication failed: Authentication failed");
    }

    @Test
    @DisplayName("Should handle BadCredentialsException with UNAUTHORIZED status")
    void shouldHandleBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException("Bad credentials");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBadCredentialsException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(401);
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_CREDENTIALS");
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid credentials");
    }

    @Test
    @DisplayName("Should handle AccessDeniedException with FORBIDDEN status")
    void shouldHandleAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAccessDeniedException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(403);
        assertThat(response.getBody().getCode()).isEqualTo("ACCESS_DENIED");
        assertThat(response.getBody().getMessage()).isEqualTo("Access denied. You do not have permission to access this resource");
    }

    @Test
    @DisplayName("Should handle generic Exception with INTERNAL_SERVER_ERROR status")
    void shouldHandleGenericException() {
        Exception exception = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getCode()).isEqualTo("INTERNAL_SERVER_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("Internal server error. Please try again later");
    }

    @Test
    @DisplayName("Should create error response with all required fields")
    void shouldCreateErrorResponseWithAllRequiredFields() {
        UserNotFoundException exception = new UserNotFoundException("Test message");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserNotFoundException(exception, request);

        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getMessage()).isNotNull();
        assertThat(errorResponse.getCode()).isNotNull();
        assertThat(errorResponse.getStatus()).isPositive();
        assertThat(errorResponse.getPath()).isNotNull();
        assertThat(errorResponse.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle exception with default constructor")
    void shouldHandleExceptionWithDefaultConstructor() {
        InvalidEmailException exception = new InvalidEmailException();

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidEmailException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid email format");
    }

    @Test
    @DisplayName("Should handle exception with custom message")
    void shouldHandleExceptionWithCustomMessage() {
        InvalidEmailException exception = new InvalidEmailException("Custom email error message");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidEmailException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Custom email error message");
    }

    @Test
    @DisplayName("Should handle multiple validation errors in MethodArgumentNotValidException")
    void shouldHandleMultipleValidationErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError nameError = new FieldError("user", "name", "Name is required");
        FieldError emailError = new FieldError("user", "email", "Invalid email format");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(nameError, emailError));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFieldErrors()).hasSize(2);
        assertThat(response.getBody().getFieldErrors()).containsEntry("name", "Name is required");
        assertThat(response.getBody().getFieldErrors()).containsEntry("email", "Invalid email format");
    }

    @Test
    @DisplayName("Should handle UserNotFoundException with different field types")
    void shouldHandleUserNotFoundWithDifferentFieldTypes() {
        UserNotFoundException exception = new UserNotFoundException("email", "test@test.com");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserNotFoundException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("User not found with email: 'test@test.com'");
    }

    @Test
    @DisplayName("Should handle UserNotFoundException with default constructor")
    void shouldHandleUserNotFoundWithDefaultConstructor() {
        UserNotFoundException exception = new UserNotFoundException();

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserNotFoundException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("User not found");
    }

    @Test
    @DisplayName("Should handle UserTypeNotFoundException with different field types")
    void shouldHandleUserTypeNotFoundWithDifferentFieldTypes() {
        UserTypeNotFoundException exception = new UserTypeNotFoundException("id", "123e4567-e89b-12d3-a456-426614174000");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserTypeNotFoundException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("UserType not found with id: '123e4567-e89b-12d3-a456-426614174000'");
    }

    @Test
    @DisplayName("Should handle UserTypeNotFoundException with default constructor")
    void shouldHandleUserTypeNotFoundWithDefaultConstructor() {
        UserTypeNotFoundException exception = new UserTypeNotFoundException();

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserTypeNotFoundException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("UserType not found");
    }

    @Test
    @DisplayName("Should handle AddressNotFoundException with default constructor")
    void shouldHandleAddressNotFoundWithDefaultConstructor() {
        AddressNotFoundException exception = new AddressNotFoundException();

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAddressNotFoundException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Address not found");
    }

    @Test
    @DisplayName("Should handle InvalidPasswordException with default constructor")
    void shouldHandleInvalidPasswordWithDefaultConstructor() {
        InvalidPasswordException exception = new InvalidPasswordException();

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidPasswordException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Password does not meet security requirements");
    }

    @Test
    @DisplayName("Should handle InvalidDataException with field and value constructor")
    void shouldHandleInvalidDataWithFieldAndValue() {
        InvalidDataException exception = new InvalidDataException("birthDate", "invalid-date-format");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidDataException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Invalid data for field 'birthDate': invalid-date-format");
    }

    @Test
    @DisplayName("Should handle InvalidDataException with default constructor")
    void shouldHandleInvalidDataWithDefaultConstructor() {
        InvalidDataException exception = new InvalidDataException();

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidDataException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid data provided");
    }

    @Test
    @DisplayName("Should handle BusinessException with cause")
    void shouldHandleBusinessExceptionWithCause() {
        RuntimeException cause = new RuntimeException("Root cause");
        BusinessException exception = new BusinessException("Business error", cause);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Business error");
    }

    @Test
    @DisplayName("Should preserve timestamp consistency in error response")
    void shouldPreserveTimestampConsistency() {
        UserNotFoundException exception = new UserNotFoundException("Test");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserNotFoundException(exception, request);

        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getTimestamp()).isNotNull();
        assertThat(errorResponse.getTimestamp()).isBefore(java.time.OffsetDateTime.now().plusSeconds(1));
        assertThat(errorResponse.getTimestamp()).isAfter(java.time.OffsetDateTime.now().minusSeconds(5));
    }

    @Test
    @DisplayName("Should handle EmailAlreadyExistsException with default constructor")
    void shouldHandleEmailAlreadyExistsWithDefaultConstructor() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException();

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleEmailAlreadyExistsException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Email is already registered");
    }

    @Test
    @DisplayName("Should handle LoginAlreadyExistsException with default constructor")
    void shouldHandleLoginAlreadyExistsWithDefaultConstructor() {
        LoginAlreadyExistsException exception = new LoginAlreadyExistsException();

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleLoginAlreadyExistsException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Login is already registered");
    }

    @Test
    @DisplayName("Should handle exception with null message")
    void shouldHandleExceptionWithNullMessage() {
        BusinessException exception = new BusinessException(null);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("BUSINESS_RULE_VIOLATION");
    }

    @Test
    @DisplayName("Should handle nested exception messages properly")
    void shouldHandleNestedExceptions() {
        RuntimeException rootCause = new RuntimeException("Database connection failed");
        InfrastructureException exception = new InfrastructureException("Service unavailable", rootCause);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInfrastructureException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("An infrastructure error occurred. Please try again later.");
    }

    @Test
    @DisplayName("Should handle validation exception with empty errors list")
    void shouldHandleValidationExceptionWithEmptyErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of());

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFieldErrors()).isEmpty();
        assertThat(response.getBody().getMessage()).isEqualTo("Data validation error");
    }

    @Test
    @DisplayName("Should handle AuthenticationException with null message")
    void shouldHandleAuthenticationExceptionWithNullMessage() {
        AuthenticationException exception = mock(AuthenticationException.class);
        when(exception.getMessage()).thenReturn(null);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAuthenticationException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Authentication failed: null");
    }

    @Test
    @DisplayName("Should maintain proper HTTP status codes for all exceptions")
    void shouldMaintainProperStatusCodes() {
        // Test all specific status codes
        assertThat(globalExceptionHandler.handleUserNotFoundException(new UserNotFoundException(), request)
                .getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(globalExceptionHandler.handleEmailAlreadyExistsException(new EmailAlreadyExistsException(), request)
                .getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        assertThat(globalExceptionHandler.handleInvalidEmailException(new InvalidEmailException(), request)
                .getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(globalExceptionHandler.handleUnauthorizedAccessException(new UnauthorizedAccessException(), request)
                .getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        assertThat(globalExceptionHandler.handleInfrastructureException(new InfrastructureException("Test error"), request)
                .getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Should handle exception chaining properly")
    void shouldHandleExceptionChaining() {
        Exception rootCause = new IllegalArgumentException("Root cause");
        BusinessException businessException = new BusinessException("Business layer error", rootCause);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessException(businessException, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Business layer error");
    }
}
