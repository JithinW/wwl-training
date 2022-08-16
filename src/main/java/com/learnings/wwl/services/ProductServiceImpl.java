package com.learnings.wwl.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnings.wwl.model.Product;
import com.learnings.wwl.repository.ProductRepo;

@Service
public class ProductServiceImpl implements ProductService{

	
	@Autowired
	ProductRepo repo;
	
	
	public List<Product> findAll() {
		return repo.findAll();
	}
	

	@Override
	public Product saveProduct(Product product) {
		return repo.save(product);
	}



}
