package com.micasa.backend.apirest.clients.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.micasa.backend.apirest.commons.shared.FilesNameProcesser;

@Service
public class UploadFileServiceImp implements UploadFileService {

	private final Logger logger = LoggerFactory.getLogger(UploadFileServiceImp.class);
	private final String UPLOADS_DIR = "uploads";
	private final String IMAGES_DIR = "src/main/resources/static/img";
	private final String NOT_USER_PNG = "not_user.png";

	@Override
	public Resource load(String name) throws MalformedURLException {
		Path rutaArchivo = this.getPath(name);
		logger.info(rutaArchivo.toString());
		Resource resource = new UrlResource(rutaArchivo.toUri());
		if (!resource.exists() && !resource.isReadable()) {
			Path notUser = Paths.get(IMAGES_DIR).resolve(NOT_USER_PNG).toAbsolutePath();
			resource = new UrlResource(notUser.toUri());
			logger.error("No se pudo cargar la imagen: ".concat(name));
		}
		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();
		String finalFileName = FilesNameProcesser.cleanString(fileName);
		Path rutaArchivo = this.getPath(finalFileName);
		logger.info(rutaArchivo.toString());
		Files.copy(file.getInputStream(), rutaArchivo);
		return finalFileName;
	}

	@Override
	public boolean delete(String name) {
		if (name != null && name.length() > 0) {
			Path oldPhotoPath = this.getPath(name);
			File oldPhotoFile = oldPhotoPath.toFile();
			if (oldPhotoFile.exists() && oldPhotoFile.canRead()) {
				oldPhotoFile.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String name) {
		return Paths.get(UPLOADS_DIR).resolve(name).toAbsolutePath();
	}

}
