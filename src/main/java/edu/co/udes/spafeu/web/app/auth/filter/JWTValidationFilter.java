package edu.co.udes.spafeu.web.app.auth.filter;

import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.CONTENT_TYPE;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.HEADER_AUTHORIZATION;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.MESSAGE;
import static edu.co.udes.spafeu.web.app.util.SpringSecurityConstantes.PREFIX_TOKEN;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.co.udes.spafeu.web.app.auth.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTValidationFilter extends BasicAuthenticationFilter{

	private JWTService jWTService;

	public JWTValidationFilter(AuthenticationManager authenticationManager,JWTService jWTService) {
		super(authenticationManager);
		this.jWTService=jWTService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String header= request.getHeader(HEADER_AUTHORIZATION);
		
		if(!requiresAuthentication(header)) {
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = null;
		
		if(jWTService.validate(header,response)) {
				
			authentication = new UsernamePasswordAuthenticationToken(jWTService.getUsername(header),null, jWTService.getRoles(header));
			Long userId = jWTService.getUserId(header);
			authentication.setDetails(userId);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		}else {
			Map<String,String> body= new HashMap<>();
			body.put(MESSAGE, "el token JWT es inválido");
			
			response.getWriter().write(new ObjectMapper().writeValueAsString(body));
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(CONTENT_TYPE);
		}
			
		
		
	}
	
	protected boolean requiresAuthentication(String header) {
		
		return header != null && header.startsWith(PREFIX_TOKEN);
	}
	
	

}
