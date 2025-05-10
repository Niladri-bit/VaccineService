package com.assignment.vaccinationservice.exceptions;

public class DriveCreationFailedException extends RuntimeException{

	private static final long serialVersionUID = -879346758518365735L;


    public DriveCreationFailedException(String message) {
        super(message);
    }
}
