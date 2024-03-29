package com.micasa.backend.apirest.clients.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {

	public Resource load(String name) throws MalformedURLException;

	public String copy(MultipartFile file) throws IOException;

	public boolean delete(String name);

	public Path getPath(String name);
}
