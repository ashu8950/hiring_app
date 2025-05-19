package com.example.onboarding.service.imp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	@Value("${file.upload-dir}")
	private String uploadDir;

	public String storeFile(MultipartFile file) {
		String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
		String fileName = "Document-" + originalFileName;
		return storeFile(file, fileName);
	}

	public String storeFile(MultipartFile file, String customFileName) {
		try {
			Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
			Files.createDirectories(uploadPath);

			Path targetLocation = uploadPath.resolve(customFileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return customFileName;
		} catch (IOException e) {
			throw new RuntimeException("Could not store file " + customFileName, e);
		}
	}

	public void deleteFile(String fileName) {
		try {
			Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
			Files.deleteIfExists(filePath);
		} catch (IOException e) {
			throw new RuntimeException("Could not delete file " + fileName, e);
		}
	}
}
