package com.rpgproject.application.dto.responsebody;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ResponseViewModel<T>(
	@JsonInclude(JsonInclude.Include.NON_NULL) T data,
	@JsonInclude(JsonInclude.Include.NON_NULL) String error
) {

}
