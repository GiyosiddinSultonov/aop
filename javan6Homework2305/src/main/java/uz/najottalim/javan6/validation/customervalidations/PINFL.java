package uz.najottalim.javan6.validation.customervalidations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import uz.najottalim.javan6.validation.validators.PINFLValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = PINFLValidator.class)
public @interface PINFL {
    String message() default "Pinflda xatolik";
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
