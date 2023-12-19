package vtb.courses.stage2.logic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация LogTransformation служит для пометки компонентов реализующих метод проверки элментаов лога
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogTransformation {
    String value() default "";
}
