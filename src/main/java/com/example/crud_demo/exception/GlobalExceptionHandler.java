package com.example.crud_demo.exception;

import com.example.crud_demo.model.ErrorResponse;
import com.example.crud_demo.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

/**
 * This class acts as a global interceptor for exceptions thrown by any @RestController.
 * It ensures that the client receives a structured JSON response instead of a raw error page/code.
 * @RestControllerAdvice combines @ControllerAdvice and @ResponseBody.
 * this approach ensure the separation of concerns
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Handles specific domain exceptions, in this case ProductNotFoundException.
     * When this exception is thrown anywhere in the app, this method captures it
     * and returns our custom ProductNotFoundException.
     * @return A ResponseEntity containing the structured ErrorResponse.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) { //instead of the built in ErrorResponse, here we are using our custom one
        ErrorResponse error = new ErrorResponse(
                Instant.now().getEpochSecond(),
                404,
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * A "catch-all" handler for any other unexpected exceptions (e.g., Database connection issues).
     * This is useful in case an exception we were not expecting is thrown and prevents the leaking of sensitive stack trace information to the client.
     * @return An Error with of our choice with a message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                Instant.now().getEpochSecond(),
                418, //I'm a teapot
                "sorry but something is not right"
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
