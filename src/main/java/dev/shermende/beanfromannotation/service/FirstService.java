package dev.shermende.beanfromannotation.service;

import dev.lib.annotation.DynamicBean;
import dev.shermende.beanfromannotation.handler.FirstServiceHandler;

@DynamicBean(value = "firstService", handler = FirstServiceHandler.class)
public interface FirstService {
    void first(String hello);
}
