package com.example.book.system;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPDFURL.PDFURLValidator.class)
@Documented
public @interface ValidPDFURL {
    String message() default "Invalid PDF URL";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @NotNull
    @Pattern(regexp = "^(https?|ftp)://.*$")
    @interface URL {
        String message() default "Invalid PDF URL";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    class PDFURLValidator implements ConstraintValidator<ValidPDFURL, String> {
        private static final String[] PDF_EXTENSIONS = {".pdf"};

        @Override
        public void initialize(ValidPDFURL constraintAnnotation) {
            // Initialization, if needed
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) {
                return true; // Allow null values, if needed
            }

            // Check if the URL is valid
            if (!value.matches("^(https?|ftp)://.*$")) {
                return false;
            }

            // Check if the file extension is a PDF type
            String extension = value.substring(value.lastIndexOf('.'));
            for (String pdfExtension : PDF_EXTENSIONS) {
                if (extension.equalsIgnoreCase(pdfExtension)) {
                    return true;
                }
            }

            return false;
        }
    }
}

