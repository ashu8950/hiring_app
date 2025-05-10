package com.example.onboarding.service.imp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	@Value("${file.upload-dir}")
	private String uploadDir;

	public String storeFile(MultipartFile file) {
		String originalFileName = file.getOriginalFilename();
		String fileName = UUID.randomUUID().toString() + "-" + originalFileName;
		try {
			Path targetLocation = Paths.get(uploadDir).resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation);
			return fileName;
		} catch (IOException e) {
			throw new RuntimeException("Could not store file " + originalFileName, e);
		}
	}
}
