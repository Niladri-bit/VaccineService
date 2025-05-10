package com.assignment.vaccinationservice.exceptions;

public class UnauthorizedUserException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7191831110450703478L;

	public UnauthorizedUserException(String message) {
        super(message);
    }
}
