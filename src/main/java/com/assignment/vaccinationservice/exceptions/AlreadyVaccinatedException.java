package com.assignment.vaccinationservice.exceptions;

public class AlreadyVaccinatedException extends RuntimeException{

	private static final long serialVersionUID = -879346758518365735L;

    public AlreadyVaccinatedException(String message) {
        super(message);
    }
}
