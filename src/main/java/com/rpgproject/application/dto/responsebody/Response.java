package com.rpgproject.application.dto.responsebody;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Getter
@EqualsAndHashCode
@ToString
public class Response<OK> {

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private final Optional<OK> okViewModel;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private final Optional<String> errorMessage;

	public Response() {
		this.okViewModel = Optional.empty();
		this.errorMessage = Optional.empty();
	}

	public Response(OK okViewModel, String errorMessage) {
		this.okViewModel = Optional.ofNullable(okViewModel);
		this.errorMessage = Optional.ofNullable(errorMessage);
	}
}
