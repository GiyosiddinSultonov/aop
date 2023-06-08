package uz.najottalim.javan6.exceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.najottalim.javan6.dto.ErrorDto;
import uz.najottalim.javan6.dto.ErrorDtoMap;
import uz.najottalim.javan6.exeption.NoResourceFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto noResourceFoundExceptionHandler(NoResourceFoundException ex) {
        return ErrorDto.builder().errors(ex.getMessage()).build();
    }

    //    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorDto exceptionHandler(Exception ex){
//        return ErrorDto.builder().errors(ex.getMessage()).build();
//    }
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto dattaAccessExceptionHandler(DataAccessException ex) {
        return ErrorDto.builder().errors(ex.getMessage()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDtoMap validationHandler(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())
                ));
        return ErrorDtoMap.builder().errors(errors).build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDtoMap validationGroupHandler(ConstraintViolationException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errorsMap.merge(field, new ArrayList<>(List.of(message)), (oldList, newList) -> {
                newList.addAll(oldList);
                return newList;
            });
        }
        return ErrorDtoMap.builder().errors(errorsMap).build();
    }
}
