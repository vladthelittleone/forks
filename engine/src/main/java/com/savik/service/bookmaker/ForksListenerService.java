package com.savik.service.bookmaker;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ForksListenerService {

    @EventListener
    public void handle(final ForkFoundEvent event) {
        System.out.println("fork was found: " + event);
    }
}
