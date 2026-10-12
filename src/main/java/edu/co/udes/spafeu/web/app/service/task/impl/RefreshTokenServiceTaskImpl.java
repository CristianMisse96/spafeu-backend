package edu.co.udes.spafeu.web.app.service.task.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_ERROR_LABEL;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_SUCCESS;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.co.udes.spafeu.web.app.models.dto.MensajeDTO;
import edu.co.udes.spafeu.web.app.models.dto.RefreshTokenResponseDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.SeverityEnum;
import edu.co.udes.spafeu.web.app.service.entity.RefreshTokenRotationResult;
import edu.co.udes.spafeu.web.app.service.entity.RefreshTokenService;
import edu.co.udes.spafeu.web.app.service.task.RefreshTokenServiceTask;
import edu.co.udes.spafeu.web.app.util.Messages;
import edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Implementación ServiceTask para operaciones REST de gestión
 * de refresh tokens. Construye la {@link RespuestaServicioDTO}, delega la
 * lógica de negocio a {@link RefreshTokenService} y gestiona la cookie HttpOnly
 * del refresh token.</p>
 *
 * @author SPAFEU Backend Team
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceTaskImpl implements RefreshTokenServiceTask {

    // Keys de mensajes específicos del módulo de refresh token
    static final String KEY_REFRESH_EXITOSO = "success.refresh.token";
    static final String KEY_LOGOUT_EXITOSO = "success.logout";
    static final String KEY_ERROR_REFRESH_TOKEN_COOKIE_NO_PRESENTE = "error.refresh.token.cookie.no.presente";

    private static final String SAME_SITE_LAX = "Lax";
    private static final int REFRESH_TOKEN_MAX_AGE_SECONDS = 604800;
    private static final int COOKIE_EXPIRED_MAX_AGE = 0;
    private static final String EMPTY_COOKIE_VALUE = "";

    private final Messages messages;
    private final RefreshTokenService refreshTokenService;

    @Value("${cookie.secure:false}")
    private boolean cookieSecure;
    @Value("${cookie.refresh-token-path:/api/session}")
    private String refreshTokenPath;

    @Override
    public ResponseEntity<RespuestaServicioDTO<RefreshTokenResponseDTO>> refreshAccessToken(HttpServletRequest request,
            HttpServletResponse response) {
        String rawRefreshToken = extraerRefreshTokenCookie(request);

        if (rawRefreshToken == null) {
            return construirRespuestaError(KEY_ERROR_REFRESH_TOKEN_COOKIE_NO_PRESENTE, HttpStatus.BAD_REQUEST);
        }

        RefreshTokenRotationResult rotationResult = refreshTokenService.rotateRefreshToken(rawRefreshToken);
        RefreshTokenResponseDTO refreshTokenResponseDTO = rotationResult.refreshTokenResponse();

        agregarRefreshTokenCookie(response, rotationResult.rawRefreshToken(), REFRESH_TOKEN_MAX_AGE_SECONDS);

        RespuestaServicioDTO<RefreshTokenResponseDTO> respuesta = new RespuestaServicioDTO<>();
        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
                messages.getKey(KEY_SUCCESS), messages.getKey(KEY_REFRESH_EXITOSO)));
        respuesta.setOk(true);
        respuesta.setNegocio(refreshTokenResponseDTO);

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RespuestaServicioDTO<String>> logout(HttpServletRequest request,
            HttpServletResponse response) {
        String rawRefreshToken = extraerRefreshTokenCookie(request);

        if (rawRefreshToken == null) {
            return construirRespuestaError(KEY_ERROR_REFRESH_TOKEN_COOKIE_NO_PRESENTE, HttpStatus.BAD_REQUEST);
        }

        refreshTokenService.revokeRefreshToken(rawRefreshToken);

        agregarRefreshTokenCookie(response, EMPTY_COOKIE_VALUE, COOKIE_EXPIRED_MAX_AGE);

        RespuestaServicioDTO<String> respuesta = new RespuestaServicioDTO<>();
        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS,
                messages.getKey(KEY_SUCCESS), messages.getKey(KEY_LOGOUT_EXITOSO)));
        respuesta.setOk(true);
        respuesta.setNegocio("OK");

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    /**
     * Extrae el valor del refresh token de la cookie HttpOnly.
     *
     * @param request petición HTTP entrante
     * @return valor plano del refresh token, o {@code null} si no existe o está vacío
     */
    private String extraerRefreshTokenCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> SpringSecurityConstantes.REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .filter(value -> !value.isBlank())
                .orElse(null);
    }

    /**
     * Construye una respuesta de error genérica.
     *
     * @param <T>        tipo de dato de negocio
     * @param keyDetail  key del detalle del mensaje
     * @param httpStatus estado HTTP de la respuesta
     * @return respuesta de servicio con indicador de error
     */
    private <T> ResponseEntity<RespuestaServicioDTO<T>> construirRespuestaError(String keyDetail,
            HttpStatus httpStatus) {
        RespuestaServicioDTO<T> respuesta = new RespuestaServicioDTO<>();
        respuesta.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR,
                messages.getKey(KEY_ERROR_LABEL), messages.getKey(keyDetail)));
        respuesta.setOk(false);
        return new ResponseEntity<>(respuesta, httpStatus);
    }

    /**
     * Añade la cookie HttpOnly del refresh token a la respuesta HTTP.
     *
     * @param response respuesta HTTP saliente
     * @param value    valor plano del refresh token
     * @param maxAge   tiempo de vida en segundos
     */
    private void agregarRefreshTokenCookie(HttpServletResponse response, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(SpringSecurityConstantes.REFRESH_TOKEN_COOKIE_NAME, value)
                .path(refreshTokenPath)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(SAME_SITE_LAX)
                .maxAge(maxAge)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
