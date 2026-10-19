package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: DTO de salida con el nuevo access token y datos del usuario
 * resultante de una operación de refresh exitosa.</p>
 *
 * @author SPAFEU Backend Team
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenResponseDTO implements Serializable {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private UsuarioDTO usuario;

    private static final long serialVersionUID = 1L;
}
