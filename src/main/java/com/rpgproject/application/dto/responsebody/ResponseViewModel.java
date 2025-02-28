package com.rpgproject.application.dto.responsebody;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class ResponseViewModel<OK> {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final OK okViewModel;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final String errorMessage;

	public ResponseViewModel(OK okViewModel, String errorMessage) {
		this.okViewModel = okViewModel;
		this.errorMessage = errorMessage;
	}
}
