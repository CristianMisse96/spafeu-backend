package edu.co.udes.spafeu.web.app.service.entity.impl;

import static edu.co.udes.spafeu.web.app.util.FormatoFechaUtil.FORMATO_FECHA_DD_MM_YYYY;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.ERROR_USUARIO_ACTIVACION_NO_REGISTRADO;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.ERROR_USUARIO_ACTIVACION_TOKEN_EXPIRADO;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.ERROR_USUARIO_ACTIVACION_TOKEN_NO_VALIDO;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.ERROR_USUARIO_ACTIVACION_YA_ACTIVADO;
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

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.entities.AccountActivationToken;
import edu.co.udes.spafeu.web.app.models.entities.PasswordResetToken;
import edu.co.udes.spafeu.web.app.models.entities.Role;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.RolEnum;
import edu.co.udes.spafeu.web.app.models.enums.TemplateMailEnum;
import edu.co.udes.spafeu.web.app.models.enums.TokenStatusEnum;
import edu.co.udes.spafeu.web.app.repositories.AccountActivationTokenRepository;
import edu.co.udes.spafeu.web.app.repositories.PasswordResetTokenRepository;
import edu.co.udes.spafeu.web.app.repositories.RoleRepository;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.service.entity.AuthService;
import edu.co.udes.spafeu.web.app.service.entity.CorreoService;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Service layer para operaciones de autenticación (recuperación y reset de contraseña,
 * registro de usuario, activación de cuenta y reenvío de correo de activación).
 * Implementa la lógica de negocio para la generación de tokens de recuperación, restablecimiento
 * de contraseñas, registro con cuenta inactiva y activación de cuenta mediante token UUID.
 * </p>
 *
 * @author Sistema SPAFEU
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${email.asunto.reset.password}")
    private String forgotPasswordAsunto;
    @Value("${email.asunto.activacion.cuenta}")
    private String accountActivationAsunto;
    @Value("${email.asunto.reenvio.activacion.cuenta}")
    private String resendActivationAsunto;
    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;
    private final CorreoService correoService;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final AccountActivationTokenRepository accountActivationTokenRepository;

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
        mapa.put("frontendUrl", frontendUrl);

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
     * codificando la contraseña, asignando el rol ROLE_USER por defecto y dejando la cuenta inactiva.
     * Genera un token UUID de activación, lo persiste y envía un correo de activación al usuario.
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
        usuario.setEnabled(Boolean.FALSE);

        Role role = roleRepository.findByRol(RolEnum.ROLE_USER)
                .orElseGet(() -> {
                    Role nuevoRol = new Role();
                    nuevoRol.setRol(RolEnum.ROLE_USER);
                    return roleRepository.save(nuevoRol);
                });

        usuario.getRoles().add(role);

        Usuario usuarioGuardado = userRepository.save(usuario);

        enviarCorreoActivacion(usuarioGuardado, accountActivationAsunto);

        return modelMapper.map(usuarioGuardado, UsuarioDTO.class);
    }

    /**
     * <p>
     * Titulo: Proyecto SPAFEU
     * </p>
     * <p>
     * Descripción: Activa la cuenta de un usuario a partir de un token UUID de activación.
     * Valida que el token exista y esté activo, verifica que no haya expirado, activa la cuenta
     * del usuario y marca el token como usado.
     * </p>
     *
     * @param token Token UUID de activación enviado por correo electrónico
     * @return void
     * @throws SpafeuException Si el token no es válido o está expirado
     */
    @Override
    @Transactional
    public void activateAccount(String token) throws SpafeuException {

        AccountActivationToken tokenEntity = accountActivationTokenRepository.findByTokenAndStatus(token, TokenStatusEnum.ACTIVE)
                .orElseThrow(() -> new SpafeuException(ERROR_USUARIO_ACTIVACION_TOKEN_NO_VALIDO));

        if (tokenEntity.getExpiration().isBefore(LocalDateTime.now())) {
            tokenEntity.setStatus(TokenStatusEnum.EXPIRED);
            throw new SpafeuException(ERROR_USUARIO_ACTIVACION_TOKEN_EXPIRADO);
        }

        Usuario user = tokenEntity.getUser();
        user.setEnabled(Boolean.TRUE);
        userRepository.save(user);

        tokenEntity.setStatus(TokenStatusEnum.USED);
        accountActivationTokenRepository.save(tokenEntity);
    }

    /**
     * <p>
     * Titulo: Proyecto SPAFEU
     * </p>
     * <p>
     * Descripción: Reenvía el correo de activación de cuenta a un usuario registrado cuya cuenta aún no ha sido activada.
     * Valida que el usuario exista en el sistema y que su cuenta no esté previamente activada, luego genera un nuevo
     * token de activación, invalida los tokens previos y envía el correo con el enlace de activación.
     * </p>
     *
     * @param email Correo electrónico del usuario registrado
     * @return void
     * @throws SpafeuException Si el usuario no está registrado o la cuenta ya fue activada
     */
    @Override
    @Transactional
    public void resendActivationEmail(String email) throws SpafeuException {
        Usuario usuario = userRepository.findByUsernameOrCorreo(email)
                .orElseThrow(() -> new SpafeuException(ERROR_USUARIO_ACTIVACION_NO_REGISTRADO));

        if (Boolean.TRUE.equals(usuario.getEnabled())) {
            throw new SpafeuException(ERROR_USUARIO_ACTIVACION_YA_ACTIVADO);
        }

        enviarCorreoActivacion(usuario, resendActivationAsunto);
    }

    /**
     * <p>
     * Titulo: Proyecto SPAFEU
     * </p>
     * <p>
     * Descripción: Envía el correo de activación de cuenta generando un nuevo token UUID, invalidando los tokens
     * activos previos del usuario y delegando el envío al servicio de correo.
     * </p>
     *
     * @param usuario Usuario al que se enviará el correo de activación
     * @param asunto Asunto del correo de activación
     */
    private void enviarCorreoActivacion(Usuario usuario, String asunto) {
        accountActivationTokenRepository.invalidateTokensByUser(usuario.getId(), TokenStatusEnum.EXPIRED);

        String token = UUID.randomUUID().toString();
        AccountActivationToken activationToken = new AccountActivationToken();
        activationToken.setUser(usuario);
        activationToken.setToken(token);
        activationToken.setExpiration(LocalDateTime.now().plusHours(24));
        activationToken.setStatus(TokenStatusEnum.ACTIVE);

        accountActivationTokenRepository.save(activationToken);

        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern(FORMATO_FECHA_DD_MM_YYYY);
        String fechaFormateada = fechaActual.format(formato);

        Map<String, Object> mapa = new HashMap<>();
        mapa.put("fecha", fechaFormateada);
        mapa.put("token", token);
        mapa.put("frontendUrl", frontendUrl);

        correoService.generarCorreo(mapa, usuario.getEmail(), TemplateMailEnum.PLANTILLA_ACTIVACION_CUENTA, asunto);
    }
}
