package com.learnings.wwl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
	
	@Column(nullable = true, length = 64)
    private String img;
	private String name;
	private double price;
	private String desc;
}
