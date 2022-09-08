package com.learnings.wwl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.learnings.wwl.model.Product;
import com.learnings.wwl.repository.ProductRepo;
import com.learnings.wwl.services.ProductService;

@SpringBootTest
class WwlApplicationTests {

	@Autowired
	private ProductService service;

	@MockBean
	private ProductRepo repository;

	@Test
	public void getProductsTest() {
		when(service.findAll()).thenReturn(Stream
				.of(new Product(3, "Smart watch", "cool product", 700), new Product(4, "Mobile", "Awesome product", 1000)).collect(Collectors.toList()));
		assertEquals(2, service.findAll().size());
	}
	
	@Test
	public void saveProductTest() {
		Product product = new Product(4, "Mobile", "Awesome product", 1000);
		when(service.saveProduct(product)).thenReturn(product);
		assertEquals(product, service.saveProduct(product));
	}
	
	

}
