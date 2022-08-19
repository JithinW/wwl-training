package com.learnings.wwl.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
 
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
 
public class FileDownloadUtil {
	
    private Path foundFile;
     
    public Resource getFileAsResource(String imgName) throws IOException {
    	
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
         
        return null;
    }
}
