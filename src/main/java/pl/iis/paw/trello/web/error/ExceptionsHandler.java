package pl.iis.paw.trello.web.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.iis.paw.trello.exception.NotFoundException;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<?> handleNotFoundException(NotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exception.getMessage());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return new ResponseEntity<>(errorResponse, httpHeaders, HttpStatus.NOT_FOUND);
    }
}
