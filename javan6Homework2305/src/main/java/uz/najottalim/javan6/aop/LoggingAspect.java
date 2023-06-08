package uz.najottalim.javan6.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Pointcut("@annotation(Log)")
    public void logPointCut() {
    }

    @Before("logPointCut()")
    public void logAllMetrics() {
        log.info("muhim metodga call bo'ldi");
    }
}
