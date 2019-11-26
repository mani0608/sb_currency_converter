package com.example.currency.annotations;

import com.example.currency.annotations.validators.CurrencyCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyCodeValidator.class)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyCodeConstraint {
    String message() default "Invalid document type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
