package com.learnings.wwl.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learnings.wwl.exception.ResourceNotFoundException;
import com.learnings.wwl.model.Product;
import com.learnings.wwl.services.ProductService;



@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

	@Autowired
	ProductService productService;
	
	 
	@GetMapping("/products")
	public List<Product> getProducts() {
		return productService.findAll();
	}
	
	
	@PostMapping("/addproducts")
	public ResponseEntity<String> createEmployee(Product product, @RequestParam(value ="file",required = false) MultipartFile multipartFile){
	
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		
        product.setImg(fileName);
        
        Product savedProduct = productService.saveProduct(product);
        String uploadDir = "./product-photos/" + savedProduct.getId();
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)) {
        	try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
        }
        try(InputStream inputStream = multipartFile.getInputStream()){
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
 
        }catch(IOException e)
        {
        	System.out.println(e);
        }
		return ResponseEntity.ok("Product inserted successfully");	
	}
	
	
}

