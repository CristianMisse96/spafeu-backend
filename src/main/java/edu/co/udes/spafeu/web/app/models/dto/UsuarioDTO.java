package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import edu.co.udes.spafeu.web.app.models.enums.EstadoKycEnum;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO implements Serializable{

	private Long id;
	
	@NotBlank
	@Size(max = 30)
	private String username;
	
	@NotBlank
	@Size(max = 254)
	@Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}(\\.[a-z]+)?", flags = Pattern.Flag.CASE_INSENSITIVE)
	private String email;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotBlank
	@Size(max = 80)
	private String password;
	
	//@NotBlank
	@Size(max = 45)
	private String nombreCompleto;
	
	//@JsonProperty(access = Access.WRITE_ONLY)
	private List<RoleDTO> roles;
	private Boolean enabled;
	
	private String foto;

    private EstadoKycEnum estadoKyc;

    private NivelActividadFisicaEnum nivelActividadFisica;

    public UsuarioDTO() {
		this.roles=new ArrayList<>();
	}
	
	private static final long serialVersionUID = 1L;

}
