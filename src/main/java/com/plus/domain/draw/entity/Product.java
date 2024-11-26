package com.plus.domain.draw.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Embeddable
@NoArgsConstructor
public class Product {
	private String productName;
	private String productDescription;
	private String productImage;

	@Builder
	public Product(String productName, String productDescription, String productImage) {
		this.productName = productName;
		this.productDescription = productDescription;
		this.productImage = productImage;
	}
}