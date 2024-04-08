package com.am.product.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

	@NotEmpty(message = "productCode must Not be empty")
	private String productCode;
	
	@NotEmpty(message = "productName must Not be empty")
	private String productName;
	
	@NotEmpty(message = "productDescription must Not be empty")
	private String productDescription;
	
	private String imagUrl;
	
	@NotNull(message = "price must Not be empty")
	private Double price;
	
	private Boolean isSellable;
}
