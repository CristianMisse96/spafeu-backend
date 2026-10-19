package edu.co.udes.spafeu.web.app.auth.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import edu.co.udes.spafeu.web.app.models.dto.RoleDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.entities.UserHealthProfile;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.EstadoKycEnum;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.models.enums.RolEnum;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para JWTServiceImpl —
 * creación, validación y resolución de tokens JWT, así como
 * la búsqueda de usuario asociada al token.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de JWTServiceImpl")
class JWTServiceImplTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private HttpServletResponse response;

    private JWTServiceImpl jwtService;

    private Authentication auth;
    private UsuarioDTO userDTO;
    private Usuario usuarioEntity;
    private UserHealthProfile healthProfile;

    @BeforeEach
    void setUp() {
        SecretKey jwtSecretKey = Keys.hmacShaKeyFor(
                "spafeu-test-secret-key-32bytes-long!".getBytes(StandardCharsets.UTF_8));
        jwtService = new JWTServiceImpl(repository, jwtSecretKey);

        userDTO = new UsuarioDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@udes.edu.co");

        usuarioEntity = new Usuario();
        usuarioEntity.setId(1L);
        usuarioEntity.setUsername("testuser");
        usuarioEntity.setEmail("test@udes.edu.co");
        usuarioEntity.setPassword("encoded-password");
        usuarioEntity.setEnabled(true);
        usuarioEntity.setFoto("/uploads/foto.jpg");

        healthProfile = new UserHealthProfile();
        healthProfile.setUserId(1L);
        healthProfile.setFullName("Test User Full");
        healthProfile.setEstadoKyc(EstadoKycEnum.NO_INICIADO);
        healthProfile.setNivelActividadFisica(NivelActividadFisicaEnum.NIVEL_2);
        healthProfile.setWeightKg(new BigDecimal("70.5"));
        healthProfile.setHeightM(new BigDecimal("1.75"));
        healthProfile.setBmi(new BigDecimal("23.0"));
    }

    private void setupAuthMock() {
        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"));
        User principal = new User("testuser", "testpass", authorities);
        auth = org.mockito.Mockito.mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(principal);
        when(auth.getAuthorities()).thenAnswer(invocation -> authorities);
    }

    @Nested
    @DisplayName("resolve()")
    class Resolve {

        @Test
        @DisplayName("Debe eliminar el prefijo 'Bearer ' del token")
        void debeEliminarBearerPrefix() {
            String token = "Bearer eyJhbGciOiJIUzI1NiJ9.payload";
            String resultado = jwtService.resolve(token);

            assertThat(resultado).isEqualTo("eyJhbGciOiJIUzI1NiJ9.payload");
        }

        @Test
        @DisplayName("Debe retornar null cuando el token es null")
        void debeRetornarNullCuandoTokenNull() {
            String resultado = jwtService.resolve(null);

            assertThat(resultado).isNull();
        }

        @Test
        @DisplayName("Debe retornar null cuando el token no tiene prefijo 'Bearer '")
        void debeRetornarNullCuandoNoTieneBearerPrefix() {
            String resultado = jwtService.resolve("InvalidToken");

            assertThat(resultado).isNull();
        }

        @Test
        @DisplayName("Debe retornar null cuando el token es 'Bearer' sin espacio")
        void debeRetornarNullCuandoBearerSinEspacio() {
            String resultado = jwtService.resolve("BearerToken");

            assertThat(resultado).isNull();
        }
    }

    @Nested
    @DisplayName("create()")
    class Create {

        private String token;

        @BeforeEach
        void setUpCreate() {
            setupAuthMock();
        }

        @Test
        @DisplayName("Debe crear un token JWT válido con los datos del usuario")
        void debeCrearTokenValido() throws Exception {
            token = jwtService.create(auth, userDTO);

            assertThat(token).isNotNull();
            assertThat(token).isNotEmpty();
            assertThat(token.split("\\.")).hasSize(3);

            String resolved = jwtService.resolve("Bearer " + token);
            assertThat(resolved).isEqualTo(token);

            Claims claims = jwtService.getClaims("Bearer " + token);
            assertThat(claims).isNotNull();
            assertThat(claims.getSubject()).isEqualTo("testuser");
            assertThat(claims.get("username")).isEqualTo("testuser");
            assertThat(claims.get("id", Long.class)).isEqualTo(1L);
            assertThat(claims.getExpiration()).isNotNull();
            assertThat(claims.getIssuedAt()).isNotNull();
        }

        @Test
        @DisplayName("Debe incluir las autoridades del usuario en el token")
        void debeIncluirRolesEnToken() throws Exception {
            token = jwtService.create(auth, userDTO);

            Claims claims = jwtService.getClaims("Bearer " + token);
            Object authoritiesObj = claims.get("authorities");
            assertThat(authoritiesObj).isNotNull();
            assertThat(authoritiesObj).isInstanceOf(List.class);

            @SuppressWarnings("unchecked")
            List<Object> authoritiesList = (List<Object>) authoritiesObj;
            assertThat(authoritiesList).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("createAccessToken()")
    class CreateAccessToken {

        @Test
        @DisplayName("Debe crear access token con roles cuando el usuario tiene roles")
        void debeCrearAccessTokenConRoles() throws Exception {
            RoleDTO roleUser = new RoleDTO(1L, RolEnum.ROLE_USER);
            RoleDTO roleAdmin = new RoleDTO(2L, RolEnum.ROLE_ADMIN);
            userDTO.setRoles(List.of(roleUser, roleAdmin));

            String token = jwtService.createAccessToken(userDTO);

            assertThat(token).isNotNull();
            assertThat(token.split("\\.")).hasSize(3);

            Claims claims = jwtService.getClaims("Bearer " + token);
            assertThat(claims.getSubject()).isEqualTo("testuser");
            assertThat(claims.get("id", Long.class)).isEqualTo(1L);

            @SuppressWarnings("unchecked")
            List<Object> authorities = (List<Object>) claims.get("authorities");
            assertThat(authorities).hasSize(2);
        }

        @Test
        @DisplayName("Debe crear access token sin roles cuando el usuario no tiene roles")
        void debeCrearAccessTokenSinRoles() throws Exception {
            userDTO.setRoles(null);

            String token = jwtService.createAccessToken(userDTO);

            assertThat(token).isNotNull();
            assertThat(token.split("\\.")).hasSize(3);

            Claims claims = jwtService.getClaims("Bearer " + token);
            assertThat(claims.getSubject()).isEqualTo("testuser");

            @SuppressWarnings("unchecked")
            List<Object> authorities = (List<Object>) claims.get("authorities");
            assertThat(authorities).isEmpty();
        }
    }

    @Nested
    @DisplayName("validate()")
    class Validate {

        private String crearTokenValido() throws Exception {
            setupAuthMock();
            return jwtService.create(auth, userDTO);
        }

        @Test
        @DisplayName("Debe retornar true para un token JWT válido")
        void debeRetornarTrueParaTokenValido() throws Exception {
            String token = crearTokenValido();

            boolean resultado = jwtService.validate("Bearer " + token, response);

            assertThat(resultado).isTrue();
        }

        @Test
        @DisplayName("Debe retornar false para un token JWT inválido (malformado)")
        void debeRetornarFalseParaTokenInvalido() {
            boolean resultado = jwtService.validate("Bearer tokenInvalido", response);

            assertThat(resultado).isFalse();
        }

        @Test
        @DisplayName("Debe retornar false para un token sin prefijo Bearer")
        void debeRetornarFalseParaTokenSinBearer() throws Exception {
            String token = crearTokenValido();

            boolean resultado = jwtService.validate(token, response);

            assertThat(resultado).isFalse();
        }

        @Test
        @DisplayName("Debe retornar false cuando el token es null")
        void debeRetornarFalseCuandoTokenNull() {
            boolean resultado = jwtService.validate(null, response);

            assertThat(resultado).isFalse();
        }

        @Test
        @DisplayName("Debe retornar false para un token vacío")
        void debeRetornarFalseParaTokenVacio() {
            boolean resultado = jwtService.validate("", response);

            assertThat(resultado).isFalse();
        }
    }

    @Nested
    @DisplayName("getUsername()")
    class GetUsername {

        @Test
        @DisplayName("Debe retornar el username desde un token válido")
        void debeRetornarUsername() throws Exception {
            setupAuthMock();
            String token = jwtService.create(auth, userDTO);

            String username = jwtService.getUsername("Bearer " + token);

            assertThat(username).isEqualTo("testuser");
        }

        @Test
        @DisplayName("Debe lanzar JwtException cuando el token es inválido")
        void debeLanzarExcepcionCuandoTokenInvalido() {
            assertThrows(JwtException.class, () ->
                jwtService.getUsername("Bearer tokenInvalido"));
        }
    }

    @Nested
    @DisplayName("getUserId()")
    class GetUserId {

        @Test
        @DisplayName("Debe retornar el userId desde un token válido")
        void debeRetornarUserId() throws Exception {
            setupAuthMock();
            String token = jwtService.create(auth, userDTO);

            Long userId = jwtService.getUserId("Bearer " + token);

            assertThat(userId).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("getRoles()")
    class GetRoles {

        @Test
        @DisplayName("Debe retornar los roles desde un token válido")
        void debeRetornarRoles() throws Exception {
            setupAuthMock();
            String token = jwtService.create(auth, userDTO);

            Collection<? extends GrantedAuthority> roles =
                    jwtService.getRoles("Bearer " + token);

            assertThat(roles).isNotNull();
            assertThat(roles).isNotEmpty();
            assertThat(roles).anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
        }
    }

    @Nested
    @DisplayName("getClaims()")
    class GetClaims {

        @Test
        @DisplayName("Debe lanzar JwtException cuando el token es inválido")
        void debeLanzarExcepcionCuandoTokenInvalido() {
            assertThrows(JwtException.class, () ->
                jwtService.getClaims("Bearer tokenInvalido"));
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException cuando el token es null")
        void debeLanzarExcepcionCuandoTokenNull() {
            assertThrows(IllegalArgumentException.class, () ->
                jwtService.getClaims(null));
        }
    }

    @Nested
    @DisplayName("findUser()")
    class FindUser {

        @Test
        @DisplayName("Debe retornar UsuarioDTO con datos completos cuando el usuario existe con perfil de salud")
        void debeRetornarDtoConPerfilSalud() {
            usuarioEntity.setUsuarioKyc(healthProfile);
            when(repository.findByUsernameOrCorreo("testuser"))
                    .thenReturn(Optional.of(usuarioEntity));

            UsuarioDTO resultado = jwtService.findUser("testuser");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getFoto()).isEqualTo("/uploads/foto.jpg");
            assertThat(resultado.getEstadoKyc()).isEqualTo(EstadoKycEnum.NO_INICIADO);
            assertThat(resultado.getNivelActividadFisica()).isEqualTo(NivelActividadFisicaEnum.NIVEL_2);
            assertThat(resultado.getWeightKg()).isEqualByComparingTo(new BigDecimal("70.5"));
            assertThat(resultado.getHeightM()).isEqualByComparingTo(new BigDecimal("1.75"));
            assertThat(resultado.getBmi()).isEqualByComparingTo(new BigDecimal("23.0"));
            assertThat(resultado.getNombreCompleto()).isEqualTo("Test User Full");

            verify(repository, times(1)).findByUsernameOrCorreo("testuser");
        }

        @Test
        @DisplayName("Debe retornar UsuarioDTO sin datos de salud cuando el usuario no tiene perfil de salud")
        void debeRetornarDtoSinPerfilSalud() {
            when(repository.findByUsernameOrCorreo("testuser"))
                    .thenReturn(Optional.of(usuarioEntity));

            UsuarioDTO resultado = jwtService.findUser("testuser");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getFoto()).isEqualTo("/uploads/foto.jpg");
            assertThat(resultado.getEstadoKyc()).isEqualTo(EstadoKycEnum.NO_INICIADO);
            assertThat(resultado.getNivelActividadFisica()).isNull();
            assertThat(resultado.getWeightKg()).isNull();
            assertThat(resultado.getHeightM()).isNull();
            assertThat(resultado.getBmi()).isNull();
            assertThat(resultado.getNombreCompleto()).isNull();

            verify(repository, times(1)).findByUsernameOrCorreo("testuser");
        }

        @Test
        @DisplayName("Debe retornar UsuarioDTO vacío cuando el usuario no existe")
        void debeRetornarDtoVacioCuandoUsuarioNoExiste() {
            when(repository.findByUsernameOrCorreo("unknown"))
                    .thenReturn(Optional.empty());

            UsuarioDTO resultado = jwtService.findUser("unknown");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isNull();
            assertThat(resultado.getUsername()).isNull();
            assertThat(resultado.getEmail()).isNull();

            verify(repository, times(1)).findByUsernameOrCorreo("unknown");
        }
    }
}
