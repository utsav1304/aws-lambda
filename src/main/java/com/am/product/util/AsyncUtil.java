package com.am.product.util;

import java.net.URL;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.am.product.requests.UpdateProductRequest;
import com.am.product.service.ProductService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class AsyncUtil {
	
	
	private final ProductService productService;
	
	/**
	 * @param productCode
	 * @param url
	 */
	@Async
	public void updateProductAsync(String productCode, URL url) {
		productService.updateProduct(UpdateProductRequest.builder().productCode(productCode).imagUrl(url.toString()).build());
	}

}
