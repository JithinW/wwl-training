package com.learnings.wwl.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import com.learnings.wwl.services.ImageUploadDownloadUtil;
import com.learnings.wwl.services.ProductService;

import com.learnings.wwl.exception.ResourceNotFoundException;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

	public static final Logger log = LogManager.getLogger();

	@Autowired
	ProductService productService;

	@GetMapping("/products")
	public List<Product> getProducts() {
		return productService.findAll();
	}

	@PostMapping(value = "addproducts")
	public ResponseEntity<String> createProduct(Product product,
			@RequestParam(value = "image", required = false) MultipartFile multipartFile) throws IOException {

		Product savedProduct = productService.saveProduct(product);
		product.setImg(product.getId() + multipartFile.getOriginalFilename());
		ImageUploadDownloadUtil imageUploadUtil = new ImageUploadDownloadUtil();
		imageUploadUtil.uploadImage(savedProduct,multipartFile);
		productService.saveProduct(product);
		System.out.println("hii");
		return ResponseEntity.ok("Product inserted successfully");
	}
	

	@GetMapping("/get/{imgName}")
	public ResponseEntity<?> downloadImage(@PathVariable("imgName") String imgName)
			throws ResourceNotFoundException, IOException, URISyntaxException {

		ImageUploadDownloadUtil downloadUtil = new ImageUploadDownloadUtil();
		Resource resource = downloadUtil.getImageAsResource(imgName);
		String contentType = "image/png";
		String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
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
