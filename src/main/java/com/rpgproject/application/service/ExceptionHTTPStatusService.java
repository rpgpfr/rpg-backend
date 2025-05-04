package com.rpgproject.application.service;

import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.InvalidCredentialsException;
import com.rpgproject.domain.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ExceptionHTTPStatusService {

	public HttpStatus getHttpStatusFromExceptionClass(RuntimeException exception) {
		if (exception instanceof DuplicateException) {
			return HttpStatus.CONFLICT;
		} else if (exception instanceof NotFoundException) {
			return HttpStatus.NOT_FOUND;
		} else if (exception instanceof InvalidCredentialsException) {
			return HttpStatus.UNAUTHORIZED;
		} else if (exception instanceof InternalException) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return HttpStatus.BAD_REQUEST;
	}

}
