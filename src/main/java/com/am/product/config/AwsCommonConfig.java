package com.am.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
public class AwsCommonConfig {
	

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String accessSecret;
	
	
	
	/**
	 * @return
	 */
	@Bean
	AwsCredentialsProvider awsCredentialsProvider() {
		AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, accessSecret);
		return StaticCredentialsProvider.create(credentials);
	}
}
