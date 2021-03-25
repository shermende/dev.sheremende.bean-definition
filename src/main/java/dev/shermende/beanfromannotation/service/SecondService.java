package dev.shermende.beanfromannotation.service;

import dev.lib.annotation.DynamicBean;
import dev.shermende.beanfromannotation.handler.SecondServiceHandler;

@DynamicBean(value = "secondService", handler = SecondServiceHandler.class)
public interface SecondService {
    void second(Long hello);
}
