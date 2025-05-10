package com.assignment.vaccinationservice.exceptions;

public class DriveUpdateFailedException extends RuntimeException{

	private static final long serialVersionUID = -879346758518365735L;


    public DriveUpdateFailedException(String message) {
        super(message);
    }
}
