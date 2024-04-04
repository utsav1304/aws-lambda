package com.amway.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;

@Configuration
public class StorageConfig {

	@Value("${amazon.s3.region}")
	private String region;

	/**
	 * @param awsCredentialsProvider
	 * @return
	 */
	@Bean
	S3Client s3Client(AwsCredentialsProvider awsCredentialsProvider) {
		return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(awsCredentialsProvider)
                .build();
		
	}
	
	/**
	 * @return
	 */
	@Bean
	S3Utilities s3Utilities() {
		return S3Utilities.builder().region(Region.of(region)).build();
	}

}
