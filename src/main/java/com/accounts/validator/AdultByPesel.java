package com.accounts.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PeselValidator.class)
public @interface AdultByPesel {

    String message() default "person not adult by pesel";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
