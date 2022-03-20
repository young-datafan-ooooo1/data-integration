package com.youngdatafan.dataintegration.core.validation;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validation annotation to validate that 2 fields have the same value.
 * An array of fields and their matching confirmation fields can be supplied.
 *
 * @author gavin
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
public @interface FieldMatch {
    /**
     * 提示信息.
     *
     * @return string msg
     */
    String message() default "{constraints.fieldMatch}";

    /**
     * group.
     *
     * @return group class
     */
    Class<?>[] groups() default {};

    /**
     * payload.
     *
     * @return payload class.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 第一个字段.
     *
     * @return The first field
     */
    String first();

    /**
     * 第二个字段.
     *
     * @return The second field
     */
    String second();

    /**
     * Defines several <code>@FieldMatch</code> annotations on the same element.
     *
     * @see FieldMatch
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FieldMatch[] value();
    }
}
