package com.rpgproject.domain.exception;

public class NoResultException extends RuntimeException {

	public NoResultException() {
		super("We could not find any result.");
	}

}
