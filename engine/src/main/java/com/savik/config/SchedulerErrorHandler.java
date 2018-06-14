package com.savik.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.ErrorHandler;

@Log4j2
public class SchedulerErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
        log.error("Scheduler error:",t);
    }
}
