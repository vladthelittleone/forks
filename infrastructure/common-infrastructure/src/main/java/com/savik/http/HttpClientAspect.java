package com.savik.http;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Aspect
@Log4j2
public class HttpClientAspect {

    @Before("execution(* com.savik.http.HttpClient*.*(..))")
    public void monitor(JoinPoint jp) {
        final Object[] args = jp.getArgs();
        String url = null;
        Map dataOrHeaders = null;
        if(args.length == 1 && args[0] instanceof String) {
            url = (String) args[0];
        }
        if(args.length == 2 && args[1] instanceof Map) {
            dataOrHeaders = (Map) args[1];
        }
        log.debug(jp.getSignature().getName(), url, dataOrHeaders);
    }
}
