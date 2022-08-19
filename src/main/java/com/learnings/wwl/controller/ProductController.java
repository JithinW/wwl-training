package com.learnings.wwl.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learnings.wwl.exception.ResourceNotFoundException;
import com.learnings.wwl.model.Product;
import com.learnings.wwl.services.FileDownloadUtil;
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
	
	
	@PostMapping("addproducts")
    public ResponseEntity<String> createProduct(Product product, @RequestParam(value ="image",required = false) MultipartFile multipartFile){
   
        
        productService.saveProduct(product);
        
        product.setImg(product.getId()+ multipartFile.getOriginalFilename());
        
        String uploadDir = "./Images/";
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                
                e.printStackTrace();
            }
        }
        try(InputStream inputStream = multipartFile.getInputStream()){
        Path filePath = uploadPath.resolve(String.valueOf(product.getId())+multipartFile.getOriginalFilename());
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        }catch(IOException e)
        {
            System.out.println(e);
        }
        productService.saveProduct(product);
        return ResponseEntity.ok("Product inserted successfully");    
    }
	
	
	
//	@GetMapping("download/{id}")
//	public ResponseEntity<Resource> downloadFile(HttpServletRequest request) throws IOException
//	{	
//		Resource resource = new ClassPathResource("apple.jpg");
//		String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//		return ResponseEntity.ok()
//		        .contentType(MediaType.parseMediaType(contentType))
//		        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=apple.jpg")
//		        .body(resource);
//	}
	
	
	
    @GetMapping("/get/{imgName}")
    public ResponseEntity<?> downloadFile(@PathVariable("imgName") String imgName) {
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
         
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
         
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);       
    }
}
	

	
	

