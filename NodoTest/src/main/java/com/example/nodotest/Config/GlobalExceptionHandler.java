package com.example.nodotest.Config;

import com.example.nodotest.Dto.Response.ErrorResponse;
import com.example.nodotest.Exception.*;
import com.example.nodotest.Exception.ErrorProduct.ProductCategoryNotFoundException;
import com.example.nodotest.Exception.ErrorProduct.ProductCodeExistsException;
import com.example.nodotest.Exception.ErrorProduct.ProductNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // Xử lý exception CategoryNotFoundException
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException ex, Locale locale) {
        String errorMessage = messageSource.getMessage("category.notfound", null, locale);
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Xử lý exception CategoryCodeExistsException
    @ExceptionHandler(CategoryCodeExistsException.class)
    public ResponseEntity<Object> handleCategoryCodeExistsException(CategoryCodeExistsException ex, Locale locale) {
        String errorMessage = messageSource.getMessage("category.code.exists", null, locale);
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Xử lý exception chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, Locale locale) {
        ex.printStackTrace();
        String errorMessage = messageSource.getMessage("general.error", null, locale);
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleFileSizeExceededException(FileSizeExceededException ex, Locale locale) {
        // Lấy thông báo lỗi từ messages.properties
        String errorMessage = messageSource.getMessage("error.category.image.size", null, locale);

        // Tạo đối tượng ErrorResponse và trả về
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // catch error validate
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = messageSource.getMessage(error, locale);
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }


    // Xử lý exception ProductCodeExistsException
    @ExceptionHandler(ProductCodeExistsException.class)
    public ResponseEntity<Object> handleProductCodeExistsException(ProductCodeExistsException ex, Locale locale) {
        log.error("ProductCodeExistsException: {}", ex.getMessage(), ex);
        ex.printStackTrace();
        String errorMessage = messageSource.getMessage("product.code.exists", null, locale);
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductCategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductCategoryNotFound(ProductCategoryNotFoundException ex) {
        log.error("ProductCategoryNotFoundException: {}", ex.getMessage(), ex);
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
        log.error("ProductNotFoundException: {}", ex.getMessage(), ex);
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }



    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFile(
            InvalidFileException ex, Locale locale) {
        log.error("InvalidFileException: {}", ex.getMessage());
        String errorMessage = messageSource.getMessage("error.file.invalid",
                new Object[]{ex.getMessage()}, locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(
            ConstraintViolationException ex, Locale locale) {
        log.error("ConstraintViolationException: {}", ex.getMessage());
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // ========== HTTP/REQUEST EXCEPTIONS ==========

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, Locale locale) {
        log.error("HttpRequestMethodNotSupportedException: {}", ex.getMessage());
        String errorMessage = messageSource.getMessage("error.method.not.supported",
                new Object[]{ex.getMethod()}, locale);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, Locale locale) {
        log.error("HttpMediaTypeNotSupportedException: {}", ex.getMessage());
        String errorMessage = messageSource.getMessage("error.media.type.not.supported", null, locale);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, Locale locale) {
        log.error("HttpMessageNotReadableException: {}", ex.getMessage());
        String errorMessage = messageSource.getMessage("error.message.not.readable", null, locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(
            MissingServletRequestParameterException ex, Locale locale) {
        log.error("MissingServletRequestParameterException: {}", ex.getMessage());
        String errorMessage = messageSource.getMessage("error.missing.parameter",
                new Object[]{ex.getParameterName()}, locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, Locale locale) {
        log.error("MethodArgumentTypeMismatchException: {}", ex.getMessage());

        String paramName = ex.getName();
        Object invalidValue = ex.getValue();

        String errorMessage;
        if ("page".equals(paramName) || "size".equals(paramName)) {
            errorMessage = messageSource.getMessage("error.pagination.invalid.type",
                    new Object[]{paramName, invalidValue}, locale);
        } else {
            errorMessage = messageSource.getMessage("error.type.mismatch",
                    new Object[]{paramName, invalidValue}, locale);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex, Locale locale) {
        log.error("MaxUploadSizeExceededException: {}", ex.getMessage());
        String errorMessage = messageSource.getMessage("error.file.size.exceeded", null, locale);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(
            NoHandlerFoundException ex, Locale locale) {
        log.error("NoHandlerFoundException: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        String errorMessage = messageSource.getMessage("error.endpoint.not.found", null, locale);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(errorMessage));
    }

    // ========== RUNTIME & GENERAL EXCEPTIONS ==========

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, Locale locale) {
        log.error("IllegalArgumentException: {}", ex.getMessage());
        String errorMessage = messageSource.getMessage("error.illegal.argument",
                new Object[]{ex.getMessage()}, locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointer(
            NullPointerException ex, Locale locale) {
        log.error("NullPointerException: ", ex);
        String errorMessage = messageSource.getMessage("error.null.pointer", null, locale);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(errorMessage));
    }

    // ========== CATCH-ALL EXCEPTION HANDLER ==========

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleThrowable(
            Throwable ex, Locale locale) {
        log.error("Critical error (Throwable): ", ex);
        String errorMessage = messageSource.getMessage("error.critical", null, locale);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(errorMessage));
    }
}