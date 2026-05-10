package edu.co.udes.spafeu.web.app.service.entity.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Implementación de la clase userDetailsService spring security para validación de credenciales en login
 * </p>
 *
 * @author Cristian Misse
 **/

@Service
@RequiredArgsConstructor
public class UsuarioUserDetailsServiceImpl implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;


	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

		Optional<Usuario> userOptional= usuarioRepository.findByUsernameOrCorreo(username);

		if(userOptional.isEmpty()) {
			throw new IllegalArgumentException(String.format("Usuario %s no esta registrado", username));
		}

		Usuario usuario= userOptional.orElseThrow();

		List<GrantedAuthority> authorities= usuario.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(String.valueOf(role.getRol())))
				.collect(Collectors.toList());


		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(),true,true,true, authorities);
	}

}