package edu.co.udes.spafeu.web.app.service.entity.impl;

import java.io.IOException;
import java.net.MalformedURLException;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.service.entity.UploadFileService;
import edu.co.udes.spafeu.web.app.service.entity.UserService;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Service layer para gestión de usuarios (perfil, imagen, validaciones).
 * La autenticación (registro, recuperación de contraseña) es responsabilidad de AuthService.
 * </p>
 *
 * @author Sistema SPAFEU
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private static final String ERROR_USUARIO_NO_EXISTE_CARGAR_IMAGEN = "error.usuario.no.existe.cargar.imagen";

	private final UsuarioRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final UploadFileService uploadFileService;

	/**
	 * <p>
	 * Titulo: Proyecto SPAFEU
	 * </p>
	 * <p>
	 * Descripción: Valida si un correo electrónico ya está registrado en el sistema.
	 * </p>
	 *
	 * @param email Correo electrónico a validar
	 * @return true si el correo existe, false si no existe
	 * @throws SpafeuException Si ocurre un error durante la consulta
	 */
	@Override
	@Transactional(readOnly = true)
	public Boolean validarCorreo(String email) throws SpafeuException {

		return userRepository.existsByEmail(email);
	}

	/**
	 * <p>
	 * Titulo: Proyecto SPAFEU
	 * </p>
	 * <p>
	 * Descripción: Valida si un nombre de usuario (username) ya está registrado en el sistema.
	 * </p>
	 *
	 * @param username Nombre de usuario a validar
	 * @return true si el username existe, false si no existe
	 * @throws SpafeuException Si ocurre un error durante la consulta
	 */
	@Override
	@Transactional(readOnly = true)
	public Boolean validarUsername(String username) throws SpafeuException {
		return userRepository.existsByUsername(username);
	}

	/**
	 * <p>
	 * Titulo: Proyecto SPAFEU
	 * </p>
	 * <p>
	 * Descripción: Actualiza la imagen de perfil del usuario. Sube el nuevo archivo de imagen al sistema de archivos
	 * y elimina la imagen anterior si existe. Actualiza el campo 'foto' del usuario con el nombre del nuevo archivo.
	 * </p>
	 *
	 * @param archivo Archivo de imagen a subir (MultipartFile)
	 * @param currentUserId Identificador del usuario que actualiza la imagen
	 * @return UsuarioDTO con los datos actualizados del usuario (incluyendo nueva imagen)
	 * @throws SpafeuException Si el usuario no existe en el sistema
	 * @throws IOException Si hay error al copiar o eliminar el archivo de imagen
	 */
	@Override
	@Transactional
	public UsuarioDTO updateImageProfile(MultipartFile archivo, Long currentUserId) throws SpafeuException, IOException {

		Usuario user = userRepository.findById(currentUserId)
											 .orElseThrow(()->  new SpafeuException(ERROR_USUARIO_NO_EXISTE_CARGAR_IMAGEN));

		String nombreArchivo = uploadFileService.copiar(archivo, "uploads/usuarios/profiles/img".concat("/").concat(currentUserId.toString()));
		// eliminar si contiene una foto anterior
		String nombreFotoAnterior = user.getFoto();
		uploadFileService.eliminar(nombreFotoAnterior, "uploads/usuarios/profiles/img".concat("/").concat(currentUserId.toString()));
		// actualizar la imagen al usuario
		user.setFoto(nombreArchivo);

		return modelMapper.map(userRepository.save(user), UsuarioDTO.class);
	}

	/**
	 * <p>
	 * Titulo: Proyecto SPAFEU
	 * </p>
	 * <p>
	 * Descripción: Recupera la imagen de perfil almacenada para un usuario específico desde el sistema de archivos.
	 * </p>
	 *
	 * @param nombreRecurso Nombre del recurso (archivo de imagen)
	 * @param currentUserId Identificador del usuario
	 * @return Resource que contiene la imagen solicitada
	 * @throws SpafeuException Si el usuario no existe o el recurso no se encuentra
	 * @throws MalformedURLException Si la URL del recurso es inválida
	 */
	@Override
	public Resource obtenerImagenPerfil(String nombreRecurso, Long currentUserId) throws SpafeuException, MalformedURLException {

		return uploadFileService.cargar(nombreRecurso, "uploads/usuarios/profiles/img".concat("/").concat(currentUserId.toString()));
	}

	/**
	 * <p>
	 * Titulo: Proyecto SPAFEU
	 * </p>
	 * <p>
	 * Descripción: Busca un usuario por su identificador único.
	 * </p>
	 *
	 * @param userId Identificador del usuario a buscar
	 * @return UsuarioDTO del usuario encontrado
	 * @throws SpafeuException Si el usuario no existe
	 */
	@Override
	@Transactional(readOnly = true)
	public UsuarioDTO buscarUsuario(Long userId) throws SpafeuException {

		Usuario user = userRepository.findById(userId)
				.orElseThrow(() -> new SpafeuException(ERROR_USUARIO_NO_EXISTE_CARGAR_IMAGEN));

		return modelMapper.map(user, UsuarioDTO.class);
	}

}