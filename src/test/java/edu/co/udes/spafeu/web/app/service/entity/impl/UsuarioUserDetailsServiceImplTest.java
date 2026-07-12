package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import edu.co.udes.spafeu.web.app.models.entities.Role;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.RolEnum;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para UsuarioUserDetailsServiceImpl - carga de UserDetails para Spring Security.</p>
 * @author Sistema SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UsuarioUserDetailsServiceImpl")
class UsuarioUserDetailsServiceImplTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@InjectMocks
	private UsuarioUserDetailsServiceImpl usuarioUserDetailsService;

	private Usuario usuario;

	@BeforeEach
	void setUp() {
		// Arrange - Datos de prueba base
		usuario = new Usuario();
		usuario.setId(1L);
		usuario.setUsername("testuser");
		usuario.setEmail("test@test.com");
		usuario.setPassword("encodedPass");
		usuario.setEnabled(true);
    }

	@Nested
	@DisplayName("Tests de loadUserByUsername()")
	class LoadUserByUsernameTests {

		@Test
		@DisplayName("Debe retornar UserDetails con datos correctos cuando el usuario es encontrado con roles")
		void debeRetornarUserDetailsCorrectoCuandoUsuarioEncontradoConRoles() {
			// Arrange
			Role rolUser = new Role(RolEnum.ROLE_USER);
			Role rolAdmin = new Role(RolEnum.ROLE_ADMIN);
			usuario.setRoles(Arrays.asList(rolUser, rolAdmin));

			when(usuarioRepository.findByUsernameOrCorreo("testuser")).thenReturn(Optional.of(usuario));

			// Act
			UserDetails result = usuarioUserDetailsService.loadUserByUsername("testuser");

			// Assert
			assertThat(result).isNotNull();
			assertThat(result.getUsername()).isEqualTo("testuser");
			assertThat(result.getPassword()).isEqualTo("encodedPass");
			assertThat(result.isEnabled()).isTrue();

			Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
			assertThat(authorities).isNotNull();
			assertThat(authorities).hasSize(2);
			assertThat(authorities.stream().anyMatch(a -> "ROLE_USER".equals(a.getAuthority()))).isTrue();
			assertThat(authorities.stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()))).isTrue();

			verify(usuarioRepository).findByUsernameOrCorreo("testuser");
        }

		@Test
		@DisplayName("Debe retornar UserDetails con authorities vacía cuando el usuario no tiene roles")
		void debeRetornarUserDetailsConAuthoritiesVaciaCuandoUsuarioSinRoles() {
			// Arrange
			usuario.setRoles(Collections.emptyList());

			when(usuarioRepository.findByUsernameOrCorreo("noroles")).thenReturn(Optional.of(usuario));

			// Act
			UserDetails result = usuarioUserDetailsService.loadUserByUsername("noroles");

			// Assert
			assertThat(result).isNotNull();
			assertThat(result.getUsername()).isEqualTo("testuser");
			assertThat(result.getAuthorities()).isNotNull();
			assertThat(result.getAuthorities()).isEmpty();

			verify(usuarioRepository).findByUsernameOrCorreo("noroles");
        }

		@Test
		@DisplayName("Debe lanzar IllegalArgumentException cuando el usuario no es encontrado")
		void debeLanzarIllegalArgumentExceptionCuandoUsuarioNoEncontrado() {
			// Arrange
			when(usuarioRepository.findByUsernameOrCorreo("noexiste")).thenReturn(Optional.empty());

			// Act & Assert
			IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
					() -> usuarioUserDetailsService.loadUserByUsername("noexiste"));

			assertThat(ex.getMessage()).contains("Usuario noexiste no esta registrado");

			verify(usuarioRepository).findByUsernameOrCorreo("noexiste");
        }

		@Test
		@DisplayName("Debe retornar UserDetails con isEnabled=false cuando el usuario está deshabilitado")
		void debeRetornarUserDetailsConEnabledFalseCuandoUsuarioDeshabilitado() {
			// Arrange
			usuario.setEnabled(false);
			Role rolUser = new Role(RolEnum.ROLE_USER);
			usuario.setRoles(Collections.singletonList(rolUser));

			when(usuarioRepository.findByUsernameOrCorreo("disabledUser")).thenReturn(Optional.of(usuario));

			// Act
			UserDetails result = usuarioUserDetailsService.loadUserByUsername("disabledUser");

			// Assert
			assertThat(result).isNotNull();
			assertThat(result.isEnabled()).isFalse();
			assertThat(result.isAccountNonLocked()).isTrue();
			assertThat(result.isAccountNonExpired()).isTrue();
			assertThat(result.isCredentialsNonExpired()).isTrue();

			verify(usuarioRepository).findByUsernameOrCorreo("disabledUser");
        }

		@Test
		@DisplayName("Debe mapear correctamente múltiples roles a GrantedAuthorities")
		void debeMapearCorrectamenteMultiplesRoles() {
			// Arrange
			Role rolUser = new Role(RolEnum.ROLE_USER);
			Role rolSupervisor = new Role(RolEnum.ROLE_SUPERVISOR);
			Role rolValidador = new Role(RolEnum.ROLE_VALIDADOR);
			usuario.setRoles(Arrays.asList(rolUser, rolSupervisor, rolValidador));

			when(usuarioRepository.findByUsernameOrCorreo("multirol")).thenReturn(Optional.of(usuario));

			// Act
			UserDetails result = usuarioUserDetailsService.loadUserByUsername("multirol");

			// Assert
			assertThat(result).isNotNull();
			Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
			assertThat(authorities).isNotNull();
			assertThat(authorities).hasSize(3);
			assertThat(authorities.stream().anyMatch(a -> "ROLE_USER".equals(a.getAuthority()))).isTrue();
			assertThat(authorities.stream().anyMatch(a -> "ROLE_SUPERVISOR".equals(a.getAuthority()))).isTrue();
			assertThat(authorities.stream().anyMatch(a -> "ROLE_VALIDADOR".equals(a.getAuthority()))).isTrue();

			verify(usuarioRepository).findByUsernameOrCorreo("multirol");
        }

		@Test
		@DisplayName("Debe encontrar usuario cuando se busca por email como username")
		void debeEncontrarUsuarioCuandoSeBuscaPorEmail() {
			// Arrange
			usuario.setEmail("user@test.com");
			usuario.setUsername("userbyemail");
			Role rolUser = new Role(RolEnum.ROLE_USER);
			usuario.setRoles(Collections.singletonList(rolUser));

			when(usuarioRepository.findByUsernameOrCorreo("user@test.com")).thenReturn(Optional.of(usuario));

			// Act
			UserDetails result = usuarioUserDetailsService.loadUserByUsername("user@test.com");

			// Assert
			assertThat(result).isNotNull();
			assertThat(result.getUsername()).isEqualTo("userbyemail");
			assertThat(result.getPassword()).isEqualTo("encodedPass");
			assertThat(result.isEnabled()).isTrue();
			assertThat(result.getAuthorities()).hasSize(1);
			assertThat(result.getAuthorities().stream()
					.anyMatch(a -> "ROLE_USER".equals(a.getAuthority()))).isTrue();

			verify(usuarioRepository).findByUsernameOrCorreo("user@test.com");
        }
    }
}
