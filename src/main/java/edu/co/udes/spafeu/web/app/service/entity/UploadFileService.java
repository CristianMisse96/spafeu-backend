package edu.co.udes.spafeu.web.app.service.entity;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
	
	Resource cargar(String nombreFoto, String directorio) throws MalformedURLException;
	String copiar(MultipartFile archivo,  String directorio) throws IOException;
	boolean eliminar (String nombreFoto,  String directorio) throws IOException;
}
