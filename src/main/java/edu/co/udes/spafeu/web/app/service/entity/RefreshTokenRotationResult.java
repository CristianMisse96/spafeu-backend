package edu.co.udes.spafeu.web.app.service.entity;

import edu.co.udes.spafeu.web.app.models.dto.RefreshTokenResponseDTO;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Resultado interno de la rotación de un refresh token. Contiene
 * la respuesta pública con el access token y el nuevo refresh token en texto
 * plano para que la capa ServiceTask lo coloque en la cookie HttpOnly. NO es un
 * DTO de API y no debe serializarse como JSON.</p>
 *
 * @author SPAFEU Backend Team
 */
public record RefreshTokenRotationResult(RefreshTokenResponseDTO refreshTokenResponse, String rawRefreshToken) {
}
