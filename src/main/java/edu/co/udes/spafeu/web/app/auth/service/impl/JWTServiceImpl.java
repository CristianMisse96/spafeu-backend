package edu.co.udes.spafeu.web.app.auth.service.impl;

import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.EXPIRATION_DATE;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.PREFIX_TOKEN;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.SECRET_KEY;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.co.udes.spafeu.web.app.auth.service.JWTService;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.util.SimpleGrantedAuthorityMixin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {
	
	private final UsuarioRepository repository;

	@Override
	public String create(Authentication auth, UsuarioDTO userDTO) throws IOException {
		
		User user= (User) auth.getPrincipal();
		
		String username= user.getUsername();
		
		Collection<SimpleGrantedAuthority> roles = auth.getAuthorities().stream()
													   .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
													   .toList();
		
		Claims claims = Jwts.claims()
				.add("authorities", roles)
				.add("username", username)
				.add("id",userDTO.getId())
				.build();
		
		return Jwts.builder()
						.subject(username)
						.claims(claims)
						.expiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
						.issuedAt(new Date())
						.signWith(SECRET_KEY)
						.compact();
	}

	@Override
	public boolean validate(String token, HttpServletResponse response) {
		
		try {
			getClaims(token);
			
			return true;
		}catch(JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public Claims getClaims(String token) {
		return Jwts.parser()
				.verifyWith(SECRET_KEY).build()
				.parseSignedClaims(resolve(token))
				.getPayload();
	}

	@Override
	public String getUsername(String token) {
		
		return getClaims(token).getSubject();
	}

	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
		
		Object roles= getClaims(token).get("authorities");
		
		return Arrays.asList( 
				new ObjectMapper()
				.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
				.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
	}

	@Override
	public String resolve(String token) {
		
		 if (token == null || !token.startsWith(PREFIX_TOKEN)) {
		        return null;
		 }
		 
		 return token.substring(PREFIX_TOKEN.length());
	}

	@Override
	@Transactional(readOnly = true)
	public UsuarioDTO findUser(String username) {
	Optional<Usuario> user= repository.findByUsernameOrCorreo(username);
		
		UsuarioDTO dto= new UsuarioDTO();
		user.ifPresent(u-> {
				dto.setId(u.getId());
				dto.setFoto(u.getFoto());
				dto.setEstadoKyc(u.getEstadoKyc());
			});
		
		return dto;
	}

	@Override
	public Long getUserId(String token) {
		return getClaims(token).get("id", Long.class);
	}

}
