package dev.lib.annotation.enable;

import dev.lib.registar.DynamicBeanImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({DynamicBeanImportBeanDefinitionRegistrar.class})
public @interface EnableDynamicBeans {
    Class<?>[] basePackageClasses() default {};
}
