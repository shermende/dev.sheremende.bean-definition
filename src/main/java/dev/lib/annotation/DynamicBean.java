package dev.lib.annotation;

import java.lang.annotation.*;
import java.lang.reflect.InvocationHandler;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DynamicBean {
    String value();

    Class<? extends InvocationHandler> handler();
}
