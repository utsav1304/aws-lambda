package com.amway.product.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.amway.product.requests.UpdateProductRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

	private static final String SLASH = "/";

	@Value("${application.bucket.name}")
	private String bucketName;

	private final AmazonS3 s3Client;

	private final S3AsyncClient s3AsyncClient;

	private final ProductService productService;

	@Override
	public String uploadFile(MultipartFile file) {
		File fileObj = convertMultiPartFileToFile(file);
		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
		// fileObj.delete();
		return "File uploaded : " + fileName;
	}

	@Override
	public Mono<String> uploadFile(Path path, String productCode) {
		// AmazonS3Client awssS3Client = (AmazonS3Client) s3Client;
		File fileObj = path.toFile();
		var fileName = productCode + SLASH + fileObj.getName();

		var putObjReq = new PutObjectRequest(bucketName, fileName, fileObj);
		putObjReq.setCannedAcl(CannedAccessControlList.PublicRead);
		s3Client.putObject(putObjReq);
		// fileObj.delete();
		try {
			Files.delete(path);
		} catch (IOException e) {
			e.printStackTrace();
			return Mono.just("Error in deleting file");
		}
		// awssS3Client.getResourceUrl(bucketName, fileName);
		// awssS3Client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
		// awssS3Client.setObjectAcl(bucketName, fileName,
		// CannedAccessControlList.PublicRead);
		// var resourceUrl = awssS3Client.getResourceUrl(bucketName, fileName);
		var url = s3Client.getUrl(bucketName, fileName);
		var response = "Bucket Name :" + bucketName + ":File uploaded : " + fileName + "\n Url : " + url.toString();

		return Mono.just(response);
	}

	@Override
	public byte[] downloadFile(String fileName) {
		S3Object s3Object = s3Client.getObject(bucketName, fileName);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		try {
			return IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	@Override
	public String deleteFile(String fileName) {
		var delReq = new DeleteObjectRequest(bucketName, fileName);
		s3Client.deleteObject(delReq);
		return fileName + " removed ...";
	}

	@Override
	public Mono<String> saveFile(Flux<ByteBuffer> body, HttpHeaders headers, String productCode) {
		// String fileKey = UUID.randomUUID().toString();

		MediaType mediaType = headers.getContentType();
		var fileName = productCode + SLASH + "img";
		if (mediaType == null) {
			mediaType = MediaType.APPLICATION_OCTET_STREAM;
		}
		CompletableFuture<?> future = s3AsyncClient
				.putObject(software.amazon.awssdk.services.s3.model.PutObjectRequest.builder().bucket(bucketName)
						.contentLength(headers.getContentLength()).key(fileName).contentType(mediaType.toString())
						// .
						.acl(CannedAccessControlList.PublicRead.toString())
						// .metadata(metadata)
						.build(), AsyncRequestBody.fromPublisher(body));

		return Mono.fromFuture(future).map((response) -> {
			var url = s3Client.getUrl(bucketName, fileName);
			var imgUrl = url.toString();
			productService
					.updateProduct(UpdateProductRequest.builder().productCode(productCode).imagUrl(imgUrl).build())
					.subscribe();
			return "Bucket Name :" + bucketName + ":File uploaded : " + fileName + "\n Url : " + imgUrl;
		});
	}

	private File convertMultiPartFileToFile(MultipartFile file) {
		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
			fos.write(file.getBytes());
		} catch (IOException e) {
			log.error("Error converting multipartFile to file", e);
		}
		return convertedFile;
	}

}