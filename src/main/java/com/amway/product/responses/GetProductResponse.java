package com.amway.product.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetProductResponse extends ErrorResponse {

	

	private String productName;
	
	private String productDescription;
	
	private String imagUrl;
	
	private Double price;
	
	private Boolean isSellable;
	
	
}
