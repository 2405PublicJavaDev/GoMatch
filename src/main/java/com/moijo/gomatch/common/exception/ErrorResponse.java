package com.moijo.gomatch.common.exception;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private List<FieldError> errors;
    private String code;
    private String additionalMessage;


    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.message = code.getMessage();
        this.errors = errors;
        this.code = code.getCode();
        this.additionalMessage = "";
    }

    private ErrorResponse(final ErrorCode code) {
        this(code, "");
    }

    private ErrorResponse(final ErrorCode code, String additionalMessage) {
        this.message = code.getMessage();
        this.code = code.getCode();
        this.errors = new ArrayList<>();
        this.additionalMessage = additionalMessage;
    }


    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
        return new ErrorResponse(code, errors);
    }

    public static ErrorResponse of(final ErrorCode code, String additionalMessage) {
        return new ErrorResponse(code, additionalMessage);
    }


    @Getter
    @NoArgsConstructor
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(Set<ConstraintViolation<Object>> violations) {
            List<FieldError> errors = new ArrayList<>();
            for(ConstraintViolation<Object> violation: violations) {
                errors.add(new FieldError(violation.getPropertyPath().toString(), violation.getMessage(), violation.getMessage()));
            }
            return errors;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }


}
