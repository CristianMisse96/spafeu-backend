package edu.co.udes.spafeu.web.app.service.entity.impl;

import static edu.co.udes.spafeu.web.app.util.FormatoFechaUtil.FORMATO_FECHA_DD_MM_YYYY;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.ERROR_USUARIO_FORGOT_PASSWORD_NO_EXISTE;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.ERROR_USUARIO_REGISTRAR_EXISTE;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.ERROR_USUARIO_RESET_PASSWORD_TOKEN_EXPIRADO;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.ERROR_USUARIO_RESET_PASSWORD_TOKEN_NO_VALIDO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
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
import edu.co.udes.spafeu.web.app.service.entity.AuthService;
import edu.co.udes.spafeu.web.app.service.entity.CorreoService;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Service layer para operaciones de autenticación (recuperación y reset de contraseña).
 * Implementa la lógica de negocio para la generación de tokens de recuperación y el restablecimiento
 * de contraseñas mediante validación de tokens.
 * </p>
 *
 * @author Sistema SPAFEU
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${email.asunto.reset.password}")
    private String forgotPasswordAsunto;

    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;
    private final CorreoService correoService;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    /**
     * <p>
     * Titulo: Proyecto SPAFEU
     * </p>
     * <p>
     * Descripción: Inicia el proceso de recuperación de contraseña generando un token único,
     * almacenándolo en la base de datos y enviando un correo electrónico al usuario con el token.
     * Antes de generar un nuevo token, invalida todos los tokens activos previos del usuario.
     * </p>
     *
     * @param email Correo electrónico del usuario que solicita la recuperación
     * @return void
     * @throws SpafeuException Si el usuario no existe en el sistema o hay error en el proceso
     */
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

        // aquí empieza el envío de correo

        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern(FORMATO_FECHA_DD_MM_YYYY);
        String fechaFormateada = fechaActual.format(formato);

        Map<String, Object> mapa = new HashMap<>();
        mapa.put("fecha", fechaFormateada);
        mapa.put("token", token);

        correoService.generarCorreo(mapa, user.getEmail(), TemplateMailEnum.PLANTILLA_FORGOT_PASSWORD_USUARIO, forgotPasswordAsunto);
    }

    /**
     * <p>
     * Titulo: Proyecto SPAFEU
     * </p>
     * <p>
     * Descripción: Restablece la contraseña del usuario validando el token de recuperación.
     * Verifica que el token exista, esté activo y no haya expirado. Si es válido,
     * actualiza la contraseña del usuario (encriptada) y marca el token como usado.
     * </p>
     *
     * @param token Token de restablecimiento de contraseña
     * @param newPassword Nueva contraseña a establecer para el usuario
     * @return void
     * @throws SpafeuException Si el token es inválido, está expirado o hay error en el proceso
     */
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

    /**
     * <p>
     * Titulo: Proyecto SPAFEU
     * </p>
     * <p>
     * Descripción: Registra un nuevo usuario en el sistema validando que el email y username no existan,
     * codificando la contraseña y asignando el rol ROLE_USER por defecto.
     * </p>
     *
     * @param userDTO Datos del usuario a registrar
     * @return UsuarioDTO del usuario registrado
     * @throws SpafeuException Si el usuario ya existe (email o username duplicado)
     */
    @Override
    @Transactional
    public UsuarioDTO registrarUsuario(UsuarioDTO userDTO) throws SpafeuException {
        if (userRepository.existsByEmailOrUsername(userDTO.getEmail(), userDTO.getUsername())) {
            throw new SpafeuException(ERROR_USUARIO_REGISTRAR_EXISTE);
        }

        Usuario usuario = modelMapper.map(userDTO, Usuario.class);
        usuario.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Role role = roleRepository.findByRol(RolEnum.ROLE_USER)
                .orElseGet(() -> {
                    Role nuevoRol = new Role();
                    nuevoRol.setRol(RolEnum.ROLE_USER);
                    return roleRepository.save(nuevoRol);
                });

        usuario.getRoles().add(role);

        Usuario usuarioGuardado = userRepository.save(usuario);
        return modelMapper.map(usuarioGuardado, UsuarioDTO.class);
    }
}
