package com.am.product.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.am.product.util.AsyncUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

	private static final String SLASH = "/";

	@Value("${amazon.s3.bucket.name}")
	private String bucketName;

	private final S3Client s3Client;


	private final AsyncUtil asyncUtil;

	private final S3Utilities s3Utilities;
	
	@Override
	public String uploadFile(MultipartFile file, String productCode) {
		var fileName = productCode + SLASH + file.getOriginalFilename();
		var putReq = PutObjectRequest.builder().bucket(bucketName).key(fileName).acl(ObjectCannedACL.PUBLIC_READ)
				.contentType(file.getContentType()).contentLength(file.getSize())
				.build();
		try {
			s3Client.putObject(putReq, RequestBody.fromBytes(file.getBytes()));
		} catch (AwsServiceException | SdkClientException | IOException e) {
			e.printStackTrace();
			return "Error in Uploading file";
		}
		
		GetUrlRequest getUrlReq = GetUrlRequest.builder().bucket(bucketName).key(fileName).build();
		var url = s3Utilities.getUrl(getUrlReq);
		
		asyncUtil.updateProductAsync(productCode, url);
		return  "Bucket Name :" + bucketName + ":File uploaded : " + fileName + "\n Url : " + url.toString();
	}



	@Override
	public byte[] downloadFile(String fileName) {
		
		var s3Object = s3Client.getObject(GetObjectRequest.builder().bucket(bucketName).key(fileName).build());
		try {
			return s3Object.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error while downloading",e);
		}
		return new byte[0];
	}

	@Override
	public String deleteFile(String fileName) {
		//working
		var delReq = 
				DeleteObjectRequest.builder().bucket(bucketName).key(fileName).build();
		s3Client.deleteObject(delReq);
		return fileName + " removed ...";
	}


}