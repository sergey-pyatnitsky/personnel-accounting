package com.personnel_accounting.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger("Service logger");

    @Before("allPackagesAdvice()")
    public void beforeMethodAdvice(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.info("A method named [" + joinPoint.getTarget().getClass() + "] " +
                signature.getMethod().getName() + " has been started");
    }

    @AfterReturning("allPackagesAdvice()")
    public void afterReturningMethodAdvice(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.info("A method named [" + joinPoint.getTarget().getClass() + "] " +
                signature.getMethod().getName() + " has been stopped");
    }

    @AfterThrowing(pointcut = "allPackagesAdvice()", throwing = "e")
    public void afterThrowingMethodAdvice(JoinPoint joinPoint, Throwable e) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.error("A method named [" + joinPoint.getTarget().getClass() + "] " +
                signature.getMethod().getName() + " caused an error: " + e.getMessage());
    }

    @Pointcut("execution(* *(..)) &&\n" +
            "(\n" +
            "    within(com.personnel_accounting..department..*) ||\n" +
            "    within(com.personnel_accounting..employee..*) ||\n" +
            "    within(com.personnel_accounting..project..*) ||\n" +
            "    within(com.personnel_accounting..user..*) ||\n" +
            "    within(com.personnel_accounting..validation..*)\n" +
            ")")
    public void allPackagesAdvice() {}
}
