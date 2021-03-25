package dev.lib.factorybean;

import dev.shermende.beanfromannotation.BeanFromAnnotationApplication;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

@Data
@Slf4j
public class DynamicBeanFactoryBean implements FactoryBean<Object> {

    /**
     * properties
     */
    private Class<?> type;
    private String value;
    private InvocationHandler handler;

    /**
     * injections...
     * u can inject any bean
     */
    private BeanFromAnnotationApplication application;

    @Override
    @SneakyThrows
    public Object getObject() {
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{type},
                (proxy, method, args) -> {
                    log.info("Proxying: {} {}", method.getName(), args);
                    return handler.invoke(proxy, method, args);
                }
        );
    }

    @Override
    @SneakyThrows
    public Class<?> getObjectType() {
        return type;
    }

}
