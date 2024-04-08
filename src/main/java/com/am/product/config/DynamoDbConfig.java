package com.am.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@RequiredArgsConstructor
@Configuration
public class DynamoDbConfig {

	// https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/usecases/creating_dynamodb_web_app

	@Value("${amazon.dynamodb.region}")
	private String amazonDynamoDBRegion;

	/**
	 * @param awsCredentialsProvider
	 * @return
	 */
	@Bean
	DynamoDbClient dynamoDbClient(AwsCredentialsProvider awsCredentialsProvider) {
		var clientBuilder = DynamoDbClient.builder();

		return clientBuilder.region(Region.of(amazonDynamoDBRegion)).credentialsProvider(awsCredentialsProvider).build();
	}

	/**
	 * @param dynamoDbClient
	 * @return
	 */
	@Bean
	DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
		return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
	}

}
