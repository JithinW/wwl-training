package com.learnings.wwl.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnings.wwl.model.Product;
import com.learnings.wwl.repository.ProductRepo;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepo productRepo;

	public List<Product> findAll() {
		return productRepo.findAll();
	}

	@Override
	public Product saveProduct(Product product) {
		return productRepo.save(product);
	}

	public void deleteProductById(Long id) {
		productRepo.deleteById(id);
	}

	@Override
	public Optional<Product> findById(Long id) {
		return productRepo.findById(id);
	}

}
