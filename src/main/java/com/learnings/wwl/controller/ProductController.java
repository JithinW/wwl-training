package com.learnings.wwl.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learnings.wwl.model.Product;
import com.learnings.wwl.services.FileDownloadUtil;
import com.learnings.wwl.services.ProductService;

import com.learnings.wwl.exception.ResourceNotFoundException;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

	private static final Logger log = LogManager.getLogger();

	@Autowired
	ProductService productService;

	@GetMapping("/products")
	public List<Product> getProducts() {
		return productService.findAll();
	}

	@PostMapping("addproducts")
	public ResponseEntity<String> createProduct(Product product,
			@RequestParam(value = "image", required = true) MultipartFile multipartFile) {

		productService.saveProduct(product);

		product.setImg(product.getId() + multipartFile.getOriginalFilename());
		Path uploadPath = Paths.get("./Images/");
		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(String.valueOf(product.getId()) + multipartFile.getOriginalFilename());
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			log.error(e);
		}
		productService.saveProduct(product);
		return ResponseEntity.ok("Product inserted successfully");
	}
	

	@GetMapping("/get/{imgName}")
	public ResponseEntity<?> downloadImage(@PathVariable("imgName") String imgName) {

		FileDownloadUtil downloadUtil = new FileDownloadUtil();

		Resource resource = null;
		try {
			resource = downloadUtil.getFileAsResource(imgName);
		} catch (IOException e) {
			return ResponseEntity.internalServerError().build();
		}

		if (resource == null) {
			return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
		}

		String contentType = "image/png";
		String headerValue = "inline; filename=\"" + resource.getFilename() + "\"";

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
	}
	

	@GetMapping("/delete/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) throws ResourceNotFoundException {

		productService.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found, please check the id"));

		productService.deleteProductById(id);
		return ResponseEntity.ok("Deleted Successfully");
	}
}
