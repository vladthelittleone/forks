package com.savik.service.bookmaker;

import com.savik.domain.Match;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ThreadContextAspect {

    @Around("@annotation(MatchIdLogging)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length == 1 && args[0] instanceof Match) {
            ThreadContext.put("match.id", ((Match) args[0]).getFlashscoreId());
        }
        try {
            return joinPoint.proceed();
        } finally {
            ThreadContext.clearAll();
        }
    }
}
