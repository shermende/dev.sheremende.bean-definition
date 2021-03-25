package dev.shermende.beanfromannotation.scheduled;

import dev.shermende.beanfromannotation.service.SecondService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecondScheduled {

    private final SecondService secondService;

    @Scheduled(fixedDelay = 2000)
    public void scheduled() {
        secondService.second(System.currentTimeMillis());
    }

}
