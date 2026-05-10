package edu.co.udes.spafeu.web.app.service.entity.impl;

import static edu.co.udes.spafeu.web.app.util.FormatoFechaUtil.FORMATO_FECHA_DD_MM_YYYY;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.MailDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.entities.PasswordResetToken;
import edu.co.udes.spafeu.web.app.models.entities.Role;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.RolEnum;
import edu.co.udes.spafeu.web.app.models.enums.TemplateMailEnum;
import edu.co.udes.spafeu.web.app.models.enums.TokenStatusEnum;
import edu.co.udes.spafeu.web.app.repositories.PasswordResetTokenRepository;
import edu.co.udes.spafeu.web.app.repositories.RoleRepository;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.service.entity.EmailService;
import edu.co.udes.spafeu.web.app.service.entity.UploadFileService;
import edu.co.udes.spafeu.web.app.service.entity.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private static final String ERROR_USUARIO_NO_EXISTE_CARGAR_IMAGEN = "error.usuario.no.existe.cargar.imagen";

	@Value("${email.asunto.reset.password}")
	private String forgotPasswordAsunto;
	
	private static final String ERROR_USUARIO_FORGOT_PASSWORD_NO_EXISTE = "error.usuario.forgot.password.no.existe";
	private static final String ERROR_USUARIO_REGISTRAR_EXISTE = "error.usuario.registrar.existe";
	private static final String ERROR_USUARIO_RESET_PASSWORD_TOKEN_EXPIRADO = "error.usuario.reset.password.token.expirado";
	private static final String ERROR_USUARIO_RESET_PASSWORD_TOKEN_NO_VALIDO = "error.usuario.reset.password.token.no.valido";
	
	private final UsuarioRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final RoleRepository roleRepository;
	private final PasswordResetTokenRepository tokenRepository;
	private final EmailService emailService;
	private final UploadFileService uploadFileService;

	@Override
	public UsuarioDTO registrarUsuario(UsuarioDTO userDTO) throws SpafeuException {
		
		if (userRepository.existsByEmailOrUsername(userDTO.getEmail(), userDTO.getUsername())) {
			throw new SpafeuException(ERROR_USUARIO_REGISTRAR_EXISTE);
		}

		Usuario user = modelMapper.map(userDTO, Usuario.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Optional<Role> rolStudent = roleRepository.findByRol(RolEnum.ROLE_USER);
		rolStudent.ifPresentOrElse(r -> user.getRoles().add(r),
					() -> user.getRoles().add(new Role(RolEnum.ROLE_USER)));
		

		Usuario usuarioGuardado = userRepository.save(user);
		return modelMapper.map(usuarioGuardado, UsuarioDTO.class);
	}

	@Override
	@Transactional
	public void forgotPassword(String email) throws SpafeuException {
		
		Usuario user = userRepository.findByUsernameOrCorreo(email)
	            .orElseThrow(() -> new SpafeuException(ERROR_USUARIO_FORGOT_PASSWORD_NO_EXISTE));
		
		
		tokenRepository.invalidateTokensByUser(user.getId(), TokenStatusEnum.EXPIRED);
		
		 String token = UUID.randomUUID().toString();
	     PasswordResetToken resetToken = new PasswordResetToken();
	     resetToken.setUser(user);
	     resetToken.setToken(token);
	     resetToken.setExpiration(LocalDateTime.now().plusMinutes(15));
	     resetToken.setStatus(TokenStatusEnum.ACTIVE);

	     tokenRepository.save(resetToken);
	     
	     //aquí empieza el envío de correo
	     
	     LocalDate fechaActual = LocalDate.now();
		 DateTimeFormatter formato = DateTimeFormatter.ofPattern(FORMATO_FECHA_DD_MM_YYYY);
		 String fechaFormateada = fechaActual.format(formato);
		 
	     Map<String, Object> mapa = new HashMap<>();
	     mapa.put("fecha",fechaFormateada);
	     mapa.put("token", token);
	     
	     this.generarCorreo(mapa, user.getEmail(), TemplateMailEnum.PLANTILLA_FORGOT_PASSWORD_USUARIO);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Boolean validarCorreo(String email) throws SpafeuException {

		return userRepository.existsByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean validarUsername(String username) throws SpafeuException {
		return userRepository.existsByUsername(username);
	}
	
	@Override
	@Transactional
	public void resetPassword(String token, String newPassword) throws SpafeuException {
		
		PasswordResetToken resetToken = tokenRepository.findByTokenAndStatus(token, TokenStatusEnum.ACTIVE)
	            .orElseThrow(() -> new SpafeuException(ERROR_USUARIO_RESET_PASSWORD_TOKEN_NO_VALIDO));

	        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
	            resetToken.setStatus(TokenStatusEnum.EXPIRED);
	            throw new SpafeuException(ERROR_USUARIO_RESET_PASSWORD_TOKEN_EXPIRADO);
	        }

	        Usuario user = resetToken.getUser();
	        user.setPassword(passwordEncoder.encode(newPassword));
	        userRepository.save(user);

	        resetToken.setStatus(TokenStatusEnum.USED);
	        tokenRepository.save(resetToken);
		
	}
	
	private void generarCorreo(Map<String, Object> parametrosCorreoModelo, String usuarioCorreo, 
			TemplateMailEnum template) {

		String [] copia = null;
		
		MailDTO mail=new MailDTO(usuarioCorreo.trim().split(","),
				copia, 
				forgotPasswordAsunto,
				null,
				null,
				parametrosCorreoModelo,
				null,
				null);
		
		emailService.sendMail(mail, template);
	}



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

	@Override
	public Resource obtenerImagenPerfil(String nombreRecurso, Long currentUserId) throws SpafeuException, MalformedURLException {
		
		return uploadFileService.cargar(nombreRecurso, "uploads/usuarios/profiles/img".concat("/").concat(currentUserId.toString()));
	}

	@Override
	@Transactional(readOnly = true)
	public UsuarioDTO buscarUsuario(Long userId) throws SpafeuException {

		Usuario user = userRepository.findById(userId)
				.orElseThrow(() -> new SpafeuException(ERROR_USUARIO_NO_EXISTE_CARGAR_IMAGEN));

		return modelMapper.map(user, UsuarioDTO.class);
	}



}
