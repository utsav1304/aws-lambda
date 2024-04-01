package com.amway.product.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.amway.product.exception.ProductNotFoundException;
import com.amway.product.repostories.ProductRepo;
import com.amway.product.repostories.model.Product;
import com.amway.product.requests.CreateProductRequest;
import com.amway.product.requests.UpdateProductRequest;
import com.amway.product.responses.CreateProductResponse;
import com.amway.product.responses.GetProductResponse;
import com.amway.product.responses.UpdateProductResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepo productRepo;

	@Override
	public Mono<GetProductResponse> getProduct(String productCode) {
		log.debug(productCode);
		var productResponse = productRepo.findByProductCode(productCode)
				.switchIfEmpty(Mono.defer(() -> (Mono.error(new ProductNotFoundException(productCode)))));

		return productResponse.map(this::mapGetResponse);
	}

	@Override
	public Mono<CreateProductResponse> createProduct(CreateProductRequest createProduct) {

		return productRepo.save(mapProduct(createProduct)).map(savedProduct -> {

			var createResponse = CreateProductResponse.builder();
			createResponse.status("Success").productCode(savedProduct.getProductCode());
			return createResponse.build();

		});
	}

	@Override
	public Mono<UpdateProductResponse> updateProduct(UpdateProductRequest productRequest) {
		var productCode = productRequest.getProductCode();
		return productRepo.findById(productCode)
				.switchIfEmpty(Mono.defer(() -> (Mono.error(new ProductNotFoundException(productCode)))))
				.flatMap(product -> {
					var updatedProduct = mapUpdateProduct(product, productRequest);
					return productRepo.save(updatedProduct).map(this::buildUpdateResponse);
				});
	}

	private UpdateProductResponse buildUpdateResponse(Product savedProduct) {
		var updateResponse = UpdateProductResponse.builder();
		updateResponse.status("Success").productCode(savedProduct.getProductCode());
		return updateResponse.build();
	}

	private Product mapUpdateProduct(Product product, UpdateProductRequest productRequest) {

		if (Objects.nonNull(productRequest.getPrice())) {
			product.setPrice(productRequest.getPrice());
		}

		if (Objects.nonNull(productRequest.getImagUrl())) {
			product.setImagUrl(productRequest.getImagUrl());

		}

		if (Objects.nonNull(productRequest.getIsSellable())) {
			product.setIsSellable(productRequest.getIsSellable());
		}

		if (Objects.nonNull(productRequest.getProductName())) {
			product.setProductName(productRequest.getProductName());
		}

		if (Objects.nonNull(productRequest.getProductDescription())) {
			product.setProductDescription(productRequest.getProductDescription());
		}
		product.setNewProduct(false);

		return product;
	}

	private Product mapProduct(CreateProductRequest productRequest) {
		var product = Product.builder();

		var isSelleable = Objects.nonNull(productRequest.getIsSellable()) ? productRequest.getIsSellable()
				: Boolean.TRUE;

		product.price(productRequest.getPrice()).imagUrl(productRequest.getImagUrl()).isSellable(isSelleable);
		product.productName(productRequest.getProductName()).productDescription(productRequest.getProductDescription());
		product.productCode(productRequest.getProductCode()).newProduct(true);

		return product.build();
	}

	private GetProductResponse mapGetResponse(Product prod) {
		var getResponse = GetProductResponse.builder();

		getResponse.productCode(prod.getProductCode()).productName(prod.getProductName())
				.productDescription(prod.getProductDescription());
		getResponse.price(prod.getPrice()).imagUrl(prod.getImagUrl()).isSellable(prod.getIsSellable());
		return getResponse.build();
	}

}
