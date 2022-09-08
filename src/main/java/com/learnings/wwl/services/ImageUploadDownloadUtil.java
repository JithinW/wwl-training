package com.learnings.wwl.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.learnings.wwl.controller.ProductController;
import com.learnings.wwl.exception.ResourceNotFoundException;
import com.learnings.wwl.model.Product;
 
public class ImageUploadDownloadUtil {
	
    private Path foundFile;
     
    public Resource getImageAsResource(String imgName) throws IOException, ResourceNotFoundException {
    	
        Path dirPath = Paths.get("Images");
         
        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().equals(imgName)) {
                foundFile = file;
                return;
            }
        });
        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }
        else {
        	throw new ResourceNotFoundException("Image not found");
        }
         
    }

	public void uploadImage(Product savedProduct, MultipartFile multipartFile) throws IOException {
		
		Path uploadPath = Paths.get("./Images/");
		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(String.valueOf(savedProduct.getId()) + multipartFile.getOriginalFilename());
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			ProductController.log.error(e);
			throw new IOException("Unable to upload image right now, please try again later");

		}
		
	}

}
