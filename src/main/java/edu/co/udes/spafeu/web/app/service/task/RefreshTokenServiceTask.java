package edu.co.udes.spafeu.web.app.service.task;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.models.dto.RefreshTokenResponseDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Interfaz ServiceTask para operaciones REST de gestión de
 * refresh tokens: renovación del access token y cierre de sesión.</p>
 *
 * @author SPAFEU Backend Team
 */
public interface RefreshTokenServiceTask {

    /**
     * Renueva el access token a partir del refresh token presente en la cookie
     * HttpOnly. Rota el refresh token, establece una nueva cookie y devuelve el
     * nuevo access token en el cuerpo de la respuesta.
     *
     * @param request  petición HTTP entrante
     * @param response respuesta HTTP saliente
     * @return respuesta de servicio con el nuevo access token
     */
    ResponseEntity<RespuestaServicioDTO<RefreshTokenResponseDTO>> refreshAccessToken(HttpServletRequest request,
            HttpServletResponse response);

    /**
     * Cierra la sesión revocando el refresh token presente en la cookie HttpOnly
     * y eliminando dicha cookie.
     *
     * @param request  petición HTTP entrante
     * @param response respuesta HTTP saliente
     * @return respuesta de servicio indicando el resultado del logout
     */
    ResponseEntity<RespuestaServicioDTO<String>> logout(HttpServletRequest request, HttpServletResponse response);
}
