package com.am.product.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String status;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> error;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String productCode;

}
