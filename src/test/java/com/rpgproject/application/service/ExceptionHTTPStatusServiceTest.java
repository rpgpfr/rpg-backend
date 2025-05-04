package com.rpgproject.application.service;

import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.InvalidCredentialsException;
import com.rpgproject.domain.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionHTTPStatusServiceTest {

	private ExceptionHTTPStatusService exceptionHTTPStatusService;

	@BeforeEach
	void setUp() {
		this.exceptionHTTPStatusService = new ExceptionHTTPStatusService();
	}

	@ParameterizedTest
	@MethodSource("exceptionsProvider")
	@DisplayName("Given a domain exception, when mapping it, then the corresponding HttpStatus is returned")
	void givenADomainException_whenMappingIt_thenTheCorrespondingHttpStatusIsReturned(RuntimeException exception, HttpStatus expectedHttpStatus) {
		// Given & When
		HttpStatus actualHttpStatus = exceptionHTTPStatusService.getHttpStatusFromExceptionClass(exception);

		// Then
		assertThat(actualHttpStatus).isEqualTo(expectedHttpStatus);
	}

	private static Stream<Arguments> exceptionsProvider() {
		return Stream.of(
			Arguments.of(new DuplicateException("duplicate"), HttpStatus.CONFLICT),
			Arguments.of(new NotFoundException("not found"), HttpStatus.NOT_FOUND),
			Arguments.of(new InvalidCredentialsException("credentials"), HttpStatus.UNAUTHORIZED),
			Arguments.of(new InternalException("internal"), HttpStatus.INTERNAL_SERVER_ERROR),
			Arguments.of(new RuntimeException("duplicate"), HttpStatus.BAD_REQUEST)
		);
	}

}