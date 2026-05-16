package edu.co.udes.spafeu.web.app.service.entity;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;

public interface UserService {

	Boolean validarCorreo(String email) throws SpafeuException;

	Boolean validarUsername(String username) throws SpafeuException;

	Resource obtenerImagenPerfil(String nombreRecurso, Long currentUserId) throws SpafeuException, MalformedURLException;

	UsuarioDTO buscarUsuario(Long userId) throws SpafeuException;

	UsuarioDTO updateImageProfile(MultipartFile archivo, Long currentUserId) throws SpafeuException, IOException;

}
