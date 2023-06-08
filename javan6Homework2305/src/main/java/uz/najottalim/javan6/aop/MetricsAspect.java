package uz.najottalim.javan6.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MetricsAspect {
    @Pointcut("@annotation(Metrics)")
    public void logAnnotationPointCut() {

    }

    @Around("logAnnotationPointCut()")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("before calling");
        Long before = System.currentTimeMillis();

        Object result = proceedingJoinPoint.proceed();

        log.info("after calling");
        Long after = System.currentTimeMillis();
        log.info("{} method took, {} millisecond",proceedingJoinPoint.getSignature().getName(), after - before);
//        try {
//            Object result = proceedingJoinPoint.proceed();
//        } catch (Meth)  {
//            return
//        }
        log.info("after calling");
        return result;
    }
}
