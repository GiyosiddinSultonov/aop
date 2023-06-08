package uz.najottalim.javan6.aop;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import uz.najottalim.javan6.dto.ErrorDto;
import uz.najottalim.javan6.dto.ErrorDtoMap;
import uz.najottalim.javan6.exeption.NoResourceFoundException;

import java.util.List;
import java.util.Map;


@Component
@Aspect
@Slf4j
public class ControllerAdvice {
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void lopControllerAdvicePoinCut(){

    }

    @Around("lopControllerAdvicePoinCut()")
    public Object logClassController(ProceedingJoinPoint proceedingJoinPoint) {
        log.info("Keldi");
    try{
         return ResponseEntity.badRequest().body(proceedingJoinPoint.proceed());
    }catch (MethodArgumentNotValidException methodArgumentNotValidException){
        log.info("MethodArgumentNotValidException bo'ldi");
        return ResponseEntity.badRequest().body(ErrorDtoMap.builder().errors(methodArgumentNotValidException.getHeaders()).build());
    }catch (NoResourceFoundException noResourceFoundException){
         log.info("noResourceFoundException bo'ldi");
        return ResponseEntity.badRequest().body(ErrorDto.builder().errors(noResourceFoundException.getMessage()).build());
    }catch (DataAccessException dataAccessException){
        log.info("DataAccessException bo'ldi");
        return ResponseEntity.badRequest().body(ErrorDto.builder().errors(dataAccessException.getMessage()).build());
    }catch (ConstraintViolationException constraintViolationException){
        log.info("ConstraintViolationException bo'ldi");
        return ResponseEntity.badRequest().body(ErrorDtoMap.builder().errors((Map<String, List<String>>) constraintViolationException).build());
    }catch (Throwable throwable){
         return null;}
}
}
