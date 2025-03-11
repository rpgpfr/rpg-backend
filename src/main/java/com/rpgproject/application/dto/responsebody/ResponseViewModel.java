package com.rpgproject.application.dto.responsebody;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public record ResponseViewModel<OK>(
	@JsonInclude(JsonInclude.Include.NON_NULL) OK data,
	@JsonInclude(JsonInclude.Include.NON_NULL) String error
) {
}
