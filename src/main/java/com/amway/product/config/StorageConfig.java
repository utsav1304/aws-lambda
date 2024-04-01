package com.amway.product.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class StorageConfig {

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String accessSecret;
	@Value("${cloud.aws.region.static}")
	private String region;

	@Bean
	AmazonS3 s3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region).build();
	}
	
	
	@Bean
    S3AsyncClient s3AsyncClient() {
		AwsCredentialsProvider credentialsProvider = new AwsCredentialsProvider() {
			
			@Override
			public AwsCredentials resolveCredentials() {
				return AwsBasicCredentials.create(
						accessKey,
						accessSecret);
			}
		};
        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
          .writeTimeout(Duration.ZERO)
          .maxConcurrency(64)
          .build();
        S3Configuration serviceConfiguration = S3Configuration.builder()
          .checksumValidationEnabled(false)
          .chunkedEncodingEnabled(true)
          .build();
        S3AsyncClientBuilder b = S3AsyncClient.builder().httpClient(httpClient)
         
          .credentialsProvider(credentialsProvider)
          .serviceConfiguration(serviceConfiguration).region(Region.of(region));

        return b.build();
    }
	
	
}
