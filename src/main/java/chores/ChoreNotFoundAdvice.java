package chores;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ChoreNotFoundAdvice
{
    @ResponseBody
    @ExceptionHandler(ChoreNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String choreNotFoundHandler(ChoreNotFoundException exception)
    {
        return exception.getMessage();
    }
}
