package com.bridgelabz.fundoo.notes.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.bridgelabz.fundoo.notes.service.impl..*(..))")
    public void logBefore(JoinPoint joinPoint) {

        logger.info(
                "Method started: {}",
                joinPoint.getSignature().getName()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.bridgelabz.fundoo.notes.service.impl..*(..))",
            returning = "result"
    )
    public void logAfter(JoinPoint joinPoint, Object result) {

        logger.info(
                "Method completed: {}",
                joinPoint.getSignature().getName()
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.bridgelabz.fundoo.notes.service.impl..*(..))",
            throwing = "exception"
    )
    public void logException(JoinPoint joinPoint, Exception exception) {

        logger.error(
                "Method failed: {} - {}",
                joinPoint.getSignature().getName(),
                exception.getMessage()
        );
    }
}