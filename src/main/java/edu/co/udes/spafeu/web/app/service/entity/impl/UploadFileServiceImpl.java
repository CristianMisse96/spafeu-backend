package edu.co.udes.spafeu.web.app.service.entity.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udes.spafeu.web.app.service.entity.UploadFileService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Override
    public Resource cargar(String nombreFoto, String directorio) throws MalformedURLException {
        Path rutaArchivo = getPath(nombreFoto, directorio);
        log.info(rutaArchivo.toString());

        return new UrlResource(rutaArchivo.toUri());
    }

    @Override
    public String copiar(MultipartFile archivo, String directorio) throws IOException {
        String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
        Path rutaArchivo = getPath(nombreArchivo, directorio);
        log.info(rutaArchivo.toString());

        Files.createDirectories(rutaArchivo.getParent());
		Files.copy(archivo.getInputStream(), rutaArchivo);
		
        return nombreArchivo;
    }

    @Override
    public boolean eliminar(String nombreFoto, String directorio) throws IOException {
        if (org.springframework.util.StringUtils.hasText(nombreFoto)) {
            Path rutaFotoAnterior = getPath(nombreFoto, directorio);
            File fileAnterior = rutaFotoAnterior.toFile();

            if (fileAnterior.exists() && fileAnterior.canRead()) {
                Files.delete(rutaFotoAnterior);
                return true;
            }
        }
        return false;
    }
    

    private Path getPath(String nombreFoto, String directorio) {
    	String directoriFinal= System.getProperty("user.home") + File.separator+ directorio;
        return Paths.get(directoriFinal, nombreFoto).toAbsolutePath();
    }
}
