package com.learnings.wwl.services;

import java.util.List;

import com.learnings.wwl.model.Product;

public interface ProductService {
	
	public List<Product> findAll();
	
	public Product saveProduct(Product product);
	
	  public void deleteProductById(Long id);
	  

}
