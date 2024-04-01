package com.amway.product.repostories.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Table(name = "product")
public class Product implements Persistable<String>{

	@Id
	@Column("product_code")
	//it can be sku or anything which identifies each product uniquely
	private String productCode;
	
	@Column("product_name")
	private String productName;
	
	@Column("product_description")
	private String productDescription;
	
	@Column("imagage_url")
	private String imagUrl;
	
	@Column("price")
	private Double price;
	
	@Column("is_sellable")
	private Boolean isSellable;
	

	@Override
	public String getId() {
		return this.productCode;
	}


	@ReadOnlyProperty
    private Boolean newProduct;

    @Override
    @Transient
    public boolean isNew() {
        return this.newProduct || productCode == null;
    }

    public Product setAsNew() {
        this.newProduct = Boolean.TRUE;
        return this;
    }
}
