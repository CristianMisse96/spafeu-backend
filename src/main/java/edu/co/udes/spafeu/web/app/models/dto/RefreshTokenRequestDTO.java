package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: DTO de entrada para solicitudes de refresh token.
 * Permite recibir el token de refresco en escenarios alternativos al transporte por cookie.</p>
 *
 * @author SPAFEU Backend Team
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenRequestDTO implements Serializable {

    private String refreshToken;

    private static final long serialVersionUID = 1L;
}
