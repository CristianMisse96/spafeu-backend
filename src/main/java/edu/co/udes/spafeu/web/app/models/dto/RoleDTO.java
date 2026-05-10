package edu.co.udes.spafeu.web.app.models.dto;

import java.io.Serializable;

import edu.co.udes.spafeu.web.app.models.enums.RolEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO implements Serializable{
	
	private Long id;
	private RolEnum rol;

	private static final long serialVersionUID = 1L;
	
	
}
