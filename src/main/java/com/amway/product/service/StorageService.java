package com.amway.product.service;

import org.springframework.web.multipart.MultipartFile;


public interface StorageService {
	String uploadFile(MultipartFile file, String productCode);


	byte[] downloadFile(String fileName);

	String deleteFile(String fileName);


}
