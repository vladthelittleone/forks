package com.savik.flashscore.scheduler;

import org.springframework.core.env.Environment;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Component
public class ConditionalThreadPoolTaskScheduler extends ThreadPoolTaskScheduler {

    @Inject
    private Environment environment;

    // Override the TaskScheduler methods
    @Override
    public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
        if (!canRun()) {
            return null;
        }
        return super.schedule(task, trigger);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
        if (!canRun()) {
            return null;
        }
        return super.schedule(task, startTime);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
        if (!canRun()) {
            return null;
        }
        return super.scheduleAtFixedRate(task, startTime, period);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
        if (!canRun()) {
            return null;
        }
        return super.scheduleAtFixedRate(task, period);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
        if (!canRun()) {
            return null;
        }
        return super.scheduleWithFixedDelay(task, startTime, delay);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
        if (!canRun()) {
            return null;
        }
        return super.scheduleWithFixedDelay(task, delay);
    }

    private boolean canRun() {
        if (environment == null) {
            return false;
        }

        if (!Boolean.valueOf(environment.getProperty("scheduling.enabled"))) {
            return false;
        }

        return true;
    }
}
