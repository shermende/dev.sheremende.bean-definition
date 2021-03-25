package dev.shermende.beanfromannotation;

import dev.lib.annotation.enable.EnableDynamicBeans;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableDynamicBeans
@SpringBootApplication
@RequiredArgsConstructor
public class BeanFromAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeanFromAnnotationApplication.class, args);
    }

}
