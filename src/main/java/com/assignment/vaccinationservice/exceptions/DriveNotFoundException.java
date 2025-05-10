package com.assignment.vaccinationservice.exceptions;

public class DriveNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -879346758518365735L;


    public DriveNotFoundException(String message) {
        super(message);
    }
}
