package com.amway.product.service;

import java.nio.ByteBuffer;
import java.nio.file.Path;

import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StorageService {
	String uploadFile(MultipartFile file);

	String uploadFile(Path file, String productCode);

	byte[] downloadFile(String fileName);

	String deleteFile(String fileName);

	String saveFile(Flux<ByteBuffer> body, HttpHeaders headers, String productCode);

}
