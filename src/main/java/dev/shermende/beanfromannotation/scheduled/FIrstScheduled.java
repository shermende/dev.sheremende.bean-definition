package dev.shermende.beanfromannotation.scheduled;

import dev.shermende.beanfromannotation.service.FirstService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FIrstScheduled {

    private final FirstService firstService;

    @Scheduled(fixedDelay = 1000)
    public void scheduled() {
        firstService.first(UUID.randomUUID().toString());
    }

}
