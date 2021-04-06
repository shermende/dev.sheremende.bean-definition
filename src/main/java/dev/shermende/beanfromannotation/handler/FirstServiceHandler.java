package dev.shermende.beanfromannotation.handler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Random;

@Slf4j
@Component
public class FirstServiceHandler implements InvocationHandler {

    @Override
    @SneakyThrows
    public Object invoke(
            Object proxy,
            Method method,
            Object[] args
    ) {
        if (method.getName().equals("equals")) return this.equals(args[0]);
        if (method.getName().equals("toString")) return this.toString();
        return new Random().nextInt();
    }

}
