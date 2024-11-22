package com.plus.domain.draw.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Product {
	private String productName;
	private String productDescription;
	private String productImage;
}