package edu.co.udes.spafeu.web.app.service.entity;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Interfaz Service con lógica de negocio para autenticación, registro,
 * activación de cuenta, reenvío de correo de activación y gestión de contraseñas (recuperación y reset).
 * </p>
 *
 * @author Sistema SPAFEU
 */
public interface AuthService {

    /**
     * <p>
     * Titulo: Proyecto SPAFEU
     * </p>
     * <p>
     * Descripción: Inicia el proceso de recuperación de contraseña generando un token y enviando correo al usuario.
     * </p>
     *
     * @param email Correo electrónico del usuario
     * @return void
     * @throws SpafeuException Si el usuario no existe o hay error en el proceso
     */
    void forgotPassword(String email) throws SpafeuException;

     /**
      * <p>
      * Titulo: Proyecto SPAFEU
      * </p>
      * <p>
      * Descripción: Restablece la contraseña usando un token válido y no expirado.
      * </p>
      *
      * @param token Token de restablecimiento
      * @param newPassword Nueva contraseña a establecer
      * @return void
      * @throws SpafeuException Si el token es inválido, está expirado o hay error en el proceso
      */
     void resetPassword(String token, String newPassword) throws SpafeuException;

     /**
      * <p>
      * Titulo: Proyecto SPAFEU
      * </p>
      * <p>
      * Descripción: Registra un nuevo usuario en el sistema asignando el rol por defecto ROLE_USER.
      * El usuario se registra con la cuenta inactiva y se envía un correo con un token de activación.
      * </p>
      *
      * @param userDTO Datos del usuario a registrar
      * @return UsuarioDTO con la información del usuario registrado incluyendo su ID generado
      * @throws SpafeuException Si el correo ya está registrado, hay error en la validación o falla el proceso de registro
      */
     UsuarioDTO registrarUsuario(UsuarioDTO userDTO) throws SpafeuException;

     /**
      * <p>
      * Titulo: Proyecto SPAFEU
      * </p>
      * <p>
      * Descripción: Activa la cuenta de un usuario mediante un token UUID de activación válido y no expirado.
      * </p>
      *
      * @param token Token UUID de activación enviado por correo electrónico
      * @return void
      * @throws SpafeuException Si el token no es válido, está expirado o ya fue usado
      */
     void activateAccount(String token) throws SpafeuException;

     /**
      * <p>
      * Titulo: Proyecto SPAFEU
      * </p>
      * <p>
      * Descripción: Reenvía el correo de activación de cuenta a un usuario registrado cuya cuenta aún no ha sido activada.
      * Genera un nuevo token de activación, invalida los tokens previos y envía el correo con el enlace de activación.
      * </p>
      *
      * @param email Correo electrónico del usuario registrado
      * @return void
      * @throws SpafeuException Si el usuario no está registrado o la cuenta ya fue activada
      */
     void resendActivationEmail(String email) throws SpafeuException;
}
