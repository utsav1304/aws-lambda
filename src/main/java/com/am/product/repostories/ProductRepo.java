package com.am.product.repostories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.am.product.repostories.model.Product;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@Repository
@Slf4j
public class ProductRepo {
	
	
	private final DynamoDbEnhancedClient enhancedClient;
	
	private final DynamoDbTable<Product> workTable;
	
	public ProductRepo(DynamoDbEnhancedClient enhancedClient) {
		this.enhancedClient = enhancedClient;
		workTable = this.enhancedClient.table("Product", TableSchema.fromBean(Product.class));
	}
	

    // Put an item into a DynamoDB table.
    public Product putProduct(Product product) {

        try {
           
            workTable.putItem(product);
            return product;

        } catch (DynamoDbException e) {
            log.error("Error in saving ",e);
            throw e;
        }
    }
    
    public Optional<Product> getProduct(Product product) {

        try {
           
            return Optional.ofNullable(workTable.getItem(product));
            

        } catch (DynamoDbException e) {
            log.error("Error in getting ",e);
            throw e;
        }
    }
    
    public Product updateProduct(Product product) {

        try {
           
            return workTable.updateItem(product);

        } catch (DynamoDbException e) {
            log.error("Error in updating ",e);
            throw e;
        }
    }


	public Optional<Product> findByProductCode(String productCode) {
		return getProduct(Product.builder().productCode(productCode).build());
	}

}
