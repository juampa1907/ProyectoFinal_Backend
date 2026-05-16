package co.edu.unbosque.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerNotFoundException(ResourceNotFoundException ex){
        ErrorResponse error = new ErrorResponse(404, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorResponse> handlerGlobalException(GeneralException ex){
        ErrorResponse error = new ErrorResponse(500, "Error interno del servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handlerUnauthorizedException(UnauthorizedException ex){
        ErrorResponse error = new ErrorResponse(401, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ResourceDuplicateException.class)
    public ResponseEntity<ErrorResponse> handlerResourceDuplicateException(ResourceDuplicateException ex){
        ErrorResponse error = new ErrorResponse(409, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
