package com.amway.product.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {

	@NotEmpty(message = "productCode must Not be empty")
	private String productCode;
	
	
	private String productName;
	
	
	private String productDescription;
	
	
	private String imagUrl;
	
	
	private Double price;
	
	private Boolean isSellable;
}
