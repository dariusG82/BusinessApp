package eu.dariusgovedas.businessapp.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(CompanyNotFoundException exception){

        ErrorResponse response = new ErrorResponse();

        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setErrorMessage(exception.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
