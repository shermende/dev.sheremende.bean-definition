package dev.shermende.beanfromannotation.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
@Component
public class FirstServiceHandler implements InvocationHandler {

    @Override
    public Object invoke(
            Object proxy,
            Method method,
            Object[] args
    ) {
        log.info("{} {} {}", getClass().getSimpleName(), method.getName(), args);
        return null;
    }

}
