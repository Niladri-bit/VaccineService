package com.assignment.vaccinationservice.exceptions;

public class VaccinationEntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -879346758518365735L;


    public VaccinationEntityNotFoundException(String message) {
        super(message);
    }
}
