package edu.co.udes.spafeu.web.app.service.entity;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Interfaz Service con la lógica de negocio para la gestión de
 * refresh tokens: creación, rotación, revocación y limpieza programada.</p>
 *
 * @author SPAFEU Backend Team
 */
public interface RefreshTokenService {

    /**
     * Crea un nuevo refresh token para el usuario indicado, persiste su hash
     * SHA-256 en base de datos con estado ACTIVE y devuelve el token en texto
     * plano para que la capa superior lo coloque en la cookie HttpOnly.
     *
     * @param userId identificador del usuario
     * @return refresh token en texto plano
     * @throws SpafeuException si el usuario no existe
     */
    String createRefreshTokenValue(Long userId) throws SpafeuException;

    /**
     * Rota un refresh token válido: marca el token usado como USED, genera un
     * nuevo refresh token y un nuevo access token, y devuelve un resultado
     * interno con la respuesta pública y el nuevo refresh token en texto plano
     * para que la capa superior lo coloque en la cookie HttpOnly.
     *
     * @param rawRefreshToken refresh token en texto plano recibido desde la cookie
     * @return resultado interno con la respuesta pública y el nuevo refresh token plano
     * @throws SpafeuException si el token es inválido, expirado o reutilizado
     */
    RefreshTokenRotationResult rotateRefreshToken(String rawRefreshToken) throws SpafeuException;

    /**
     * Revoca el refresh token indicado marcándolo como REVOKED.
     *
     * @param rawRefreshToken refresh token en texto plano recibido desde la cookie
     * @throws SpafeuException si el token no existe o no está activo
     */
    void revokeRefreshToken(String rawRefreshToken) throws SpafeuException;

    /**
     * Elimina de forma programada todos los refresh tokens cuya fecha de
     * expiración sea anterior al momento actual.
     */
    void cleanupExpiredTokens();
}
