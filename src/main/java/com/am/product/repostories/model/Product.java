package com.am.product.repostories.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
@DynamoDbImmutable(builder = Product.ProductBuilder.class)
public class Product {

	private String productCode;

	private String productName;

	private String productDescription;

	private String imagUrl;

	private Double price;

	private Boolean isSellable;

	@DynamoDbPartitionKey
	public String getProductCode() {
		return productCode;
	}

	@DynamoDbSortKey
	public String getProductName() {
		return productName;
	}
}
