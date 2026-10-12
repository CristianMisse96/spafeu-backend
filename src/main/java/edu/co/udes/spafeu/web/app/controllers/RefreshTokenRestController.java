package edu.co.udes.spafeu.web.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.co.udes.spafeu.web.app.models.dto.RefreshTokenResponseDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.service.task.RefreshTokenServiceTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Controlador REST para operaciones de refresh token y cierre de
 * sesión.
 * </p>
 *
 * @author SPAFEU Backend Team
 */
@Tag(name = "Refresh Token", description = "Endpoints para renovación de access token y cierre de sesión mediante refresh tokens")
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class RefreshTokenRestController {

	private final RefreshTokenServiceTask refreshTokenServiceTask;

	/**
	 * Renueva el access token a partir del refresh token presente en la cookie
	 * HttpOnly. Rota el refresh token y establece una nueva cookie con el nuevo
	 * valor.
	 *
	 * @param request  petición HTTP entrante
	 * @param response respuesta HTTP saliente
	 * @return ResponseEntity con RespuestaServicioDTO conteniendo el nuevo access token
	 */
	@Operation(
			summary = "Renovar access token",
			description = "Renueva el access token a partir del refresh token almacenado en la cookie HttpOnly. "
					+ "Invalida el refresh token anterior y emite uno nuevo en la cookie de respuesta.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Access token renovado exitosamente"),
			@ApiResponse(responseCode = "400", description = "Cookie de refresh token no presente"),
			@ApiResponse(responseCode = "401", description = "Refresh token inválido, expirado o reutilizado"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@PostMapping(value = "/refresh")
	public ResponseEntity<RespuestaServicioDTO<RefreshTokenResponseDTO>> refresh(HttpServletRequest request,
			HttpServletResponse response) {
		return refreshTokenServiceTask.refreshAccessToken(request, response);
	}

	/**
	 * Cierra la sesión del usuario revocando el refresh token presente en la
	 * cookie HttpOnly y eliminando dicha cookie.
	 *
	 * @param request  petición HTTP entrante
	 * @param response respuesta HTTP saliente
	 * @return ResponseEntity con RespuestaServicioDTO indicando el resultado del logout
	 */
	@Operation(
			summary = "Cerrar sesión",
			description = "Revoca el refresh token almacenado en la cookie HttpOnly y elimina la cookie del cliente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sesión cerrada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Cookie de refresh token no presente"),
			@ApiResponse(responseCode = "401", description = "Refresh token inválido o no activo"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@PostMapping(value = "/logout")
	public ResponseEntity<RespuestaServicioDTO<String>> logout(HttpServletRequest request, HttpServletResponse response) {
		return refreshTokenServiceTask.logout(request, response);
	}

}
