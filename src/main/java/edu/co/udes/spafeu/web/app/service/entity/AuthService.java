package edu.co.udes.spafeu.web.app.service.entity;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Interfaz Service con lógica de negocio para autenticación y gestión de contraseñas (recuperación y reset).
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
      * </p>
      *
      * @param userDTO Datos del usuario a registrar
      * @return UsuarioDTO con la información del usuario registrado incluyendo su ID generado
      * @throws SpafeuException Si el correo ya está registrado, hay error en la validación o falla el proceso de registro
      */
     UsuarioDTO registrarUsuario(UsuarioDTO userDTO) throws SpafeuException;
}
