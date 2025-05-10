package com.assignment.vaccinationservice.exceptions;

public class DriveCreationConflictException extends RuntimeException{

	private static final long serialVersionUID = -879346758518365735L;


    public DriveCreationConflictException(String message) {
        super(message);
    }
}
