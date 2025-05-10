package com.assignment.vaccinationservice.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<String> handleUnauthorizedUserException(UnauthorizedUserException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
    
    @ExceptionHandler(DriveCreationFailedException.class)
    public ResponseEntity<String> handleDriveCreationFailedException(DriveCreationFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
    @ExceptionHandler(DriveCreationConflictException.class)
    public ResponseEntity<String> handleDriveCreationConflictException(DriveCreationConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    
    @ExceptionHandler(DriveNotFoundException.class)
    public ResponseEntity<String> handleDriveNotFoundException(DriveNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    
    @ExceptionHandler(DriveUpdateFailedException.class)
    public ResponseEntity<String> handleDriveUpdateFailedException(DriveUpdateFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
    @ExceptionHandler(VaccinationEntityNotFoundException.class)
    public ResponseEntity<String> handleVaccinationEntityNotFoundException(VaccinationEntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    
    @ExceptionHandler(AlreadyVaccinatedException.class)
    public ResponseEntity<String> handleAlreadyVaccinatedException(AlreadyVaccinatedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
