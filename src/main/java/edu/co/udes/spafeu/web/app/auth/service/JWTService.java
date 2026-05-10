package edu.co.udes.spafeu.web.app.auth.service;


import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;

public interface JWTService {
	
	public String create(Authentication auth,UsuarioDTO userDTO) throws IOException ;
	
	public boolean validate(String token, HttpServletResponse response) throws JsonProcessingException, IOException;
	
	public Claims getClaims(String token);
	
	public String getUsername(String token);
	
	Collection<? extends GrantedAuthority> getRoles(String token) throws  IOException ;
	
	public String resolve(String token);
	
	public UsuarioDTO findUser(String username);

	public Long getUserId(String token);
	
}
