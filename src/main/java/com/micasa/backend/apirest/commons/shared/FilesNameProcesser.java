package com.micasa.backend.apirest.commons.shared;

import java.text.Normalizer;
import java.util.Date;
import java.util.UUID;

public class FilesNameProcesser {

	public static String cleanString(String stringToClean) {
		String fileExtention = getFileExtension(stringToClean);
		stringToClean = getFileNameWithoutExtension(stringToClean);
		String cleanedFileName = Normalizer.normalize(stringToClean, Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		stringToClean = cleanedFileName.replace("-", "_").replace(".", "_").replaceAll("_+", "_")// Eliminar caracteres
																									// especiales
																									// duplicados
				.replaceAll("[^\\w.-]", "") // Eliminar caracteres no permitidos en una URL
				.replaceAll("\\s+", "_") // Reemplazar espacios con guiones bajos
				.replaceAll("_+", "_").concat("_").concat(UUID.randomUUID().toString()).trim().toLowerCase();
		System.out.println(stringToClean);
		return prepareStringToUrl(stringToClean).concat(fileExtention);
	}

	private static String prepareStringToUrl(String stringToClean) {
		Date fechaActual = new Date();
		Long timestamp = fechaActual.getTime();
		String fileName = getFileNameWithoutExtension(stringToClean);
		return fileName.concat("_").concat(timestamp.toString());
	}

	private static String getFileExtension(String stringToClean) {
		String fileExtention = "";
		int dotIndex = stringToClean.lastIndexOf(".");
		if (dotIndex != -1 && dotIndex < stringToClean.length() - 1) {
			fileExtention = stringToClean.substring(dotIndex);
		}
		return fileExtention;
	}

	private static String getFileNameWithoutExtension(String stringToClean) {

		int dotIndex = stringToClean.lastIndexOf(".");
		if (dotIndex != -1 && dotIndex < stringToClean.length() - 1) {
			stringToClean = stringToClean.substring(0, dotIndex);
		}
		return stringToClean;
	}
}
