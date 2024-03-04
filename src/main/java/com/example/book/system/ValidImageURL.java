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
@Constraint(validatedBy = ValidImageURL.ImageURLValidator.class)
@Documented
public @interface ValidImageURL {
    String message() default "Invalid image URL";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @NotNull
    @Pattern(regexp = "^(https?|ftp)://.*$")
    @interface URL {
        String message() default "Invalid Image URL";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    class ImageURLValidator implements ConstraintValidator<ValidImageURL, String> {
        private static final String[] IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif"};

        @Override
        public void initialize(ValidImageURL constraintAnnotation) {
            // Initialization, if needed
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.isEmpty()) {
                return true; // Allow empty or null values
            }

            // Check if the URL is valid
            if (!value.matches("^(https?|ftp)://.*$")) {
                return false;
            }

            // Check if the file extension is an image type
            String extension = value.substring(value.lastIndexOf('.'));
            for (String imageExtension : IMAGE_EXTENSIONS) {
                if (extension.equalsIgnoreCase(imageExtension)) {
                    return true;
                }
            }

            return false;
        }
    }
}
