package priv.eric.pelee.plugin;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-06 11:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Indexed
@Component
public @interface ProcessorDescriptor {

    String type();

    String description();

    String author();

    Class<?> configClass() default void.class;

}
