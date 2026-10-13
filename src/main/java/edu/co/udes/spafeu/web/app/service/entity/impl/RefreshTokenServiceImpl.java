package edu.co.udes.spafeu.web.app.service.entity.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_ERROR_GENERICO;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udes.spafeu.web.app.auth.service.JWTService;
import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.RefreshTokenResponseDTO;
import edu.co.udes.spafeu.web.app.models.dto.RoleDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.entities.RefreshToken;
import edu.co.udes.spafeu.web.app.models.entities.UserHealthProfile;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.TokenStatusEnum;
import edu.co.udes.spafeu.web.app.repositories.RefreshTokenRepository;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.service.entity.RefreshTokenRotationResult;
import edu.co.udes.spafeu.web.app.service.entity.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Implementación de {@link RefreshTokenService}. Contiene toda
 * la lógica de negocio para la gestión segura de refresh tokens: generación
 * opaca de alta entropía, almacenamiento mediante hash SHA-256, rotación
 * obligatoria, detección de reutilización y revocación.</p>
 *
 * @author SPAFEU Backend Team
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final int TOKEN_BYTE_LENGTH = 32;

    private static final String ERROR_REFRESH_TOKEN_NO_VALIDO = "error.refresh.token.no.valido";
    private static final String ERROR_REFRESH_TOKEN_EXPIRADO = "error.refresh.token.expirado";
    private static final String ERROR_REFRESH_TOKEN_REUTILIZADO = "error.refresh.token.reutilizado";
    private static final String ERROR_REFRESH_TOKEN_USUARIO_NO_EXISTE = "error.refresh.token.usuario.no.existe";

    @Value("${jwt.refresh-token.expiration-days:7}")
    private int refreshTokenExpirationDays;

    @Value("${jwt.access-token.expiration-minutes:15}")
    private int accessTokenExpirationMinutes;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final JWTService jwtService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public String createRefreshTokenValue(Long userId) throws SpafeuException {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new SpafeuException(ERROR_REFRESH_TOKEN_USUARIO_NO_EXISTE));

        String rawToken = generateRefreshTokenValue();
        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = buildRefreshToken(usuario, tokenHash);
        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    @Override
    @Transactional
    public RefreshTokenRotationResult rotateRefreshToken(String rawRefreshToken) throws SpafeuException {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw new SpafeuException(ERROR_REFRESH_TOKEN_NO_VALIDO);
        }

        String tokenHash = hashToken(rawRefreshToken);

        Optional<RefreshToken> activeToken = refreshTokenRepository
                .findByTokenHashAndStatus(tokenHash, TokenStatusEnum.ACTIVE);

        if (activeToken.isPresent()) {
            return rotateActiveToken(activeToken.get());
        }

        Optional<RefreshToken> usedToken = refreshTokenRepository
                .findByTokenHashAndStatus(tokenHash, TokenStatusEnum.USED);

        if (usedToken.isPresent()) {
            RefreshToken token = usedToken.get();
            refreshTokenRepository.invalidateTokensByUser(token.getUser().getId(), TokenStatusEnum.REVOKED);
            throw new SpafeuException(ERROR_REFRESH_TOKEN_REUTILIZADO);
        }

        throw new SpafeuException(ERROR_REFRESH_TOKEN_NO_VALIDO);
    }

    @Override
    @Transactional
    public void revokeRefreshToken(String rawRefreshToken) throws SpafeuException {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw new SpafeuException(ERROR_REFRESH_TOKEN_NO_VALIDO);
        }

        String tokenHash = hashToken(rawRefreshToken);
        RefreshToken refreshToken = refreshTokenRepository
                .findByTokenHashAndStatus(tokenHash, TokenStatusEnum.ACTIVE)
                .orElseThrow(() -> new SpafeuException(ERROR_REFRESH_TOKEN_NO_VALIDO));

        refreshToken.setStatus(TokenStatusEnum.REVOKED);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Iniciando limpieza de refresh tokens expirados");
        refreshTokenRepository.deleteByExpirationBefore(LocalDateTime.now());
    }

    private RefreshTokenRotationResult rotateActiveToken(RefreshToken activeToken) throws SpafeuException {
        if (!activeToken.getExpiration().isAfter(LocalDateTime.now())) {
            activeToken.setStatus(TokenStatusEnum.EXPIRED);
            refreshTokenRepository.save(activeToken);
            throw new SpafeuException(ERROR_REFRESH_TOKEN_EXPIRADO);
        }

        activeToken.setStatus(TokenStatusEnum.USED);
        activeToken.setUsedAt(LocalDateTime.now());
        refreshTokenRepository.save(activeToken);

        Usuario usuario = activeToken.getUser();
        
        if (Boolean.FALSE.equals(usuario.getEnabled())) {
            throw new SpafeuException(KEY_ERROR_GENERICO);
        }
        
        String newRawToken = generateRefreshTokenValue();
        String newTokenHash = hashToken(newRawToken);

        RefreshToken newRefreshToken = buildRefreshToken(usuario, newTokenHash);
        refreshTokenRepository.save(newRefreshToken);

        UsuarioDTO usuarioDTO = mapToUsuarioDTO(usuario);
				
        String accessToken = jwtService.createAccessToken(usuarioDTO);

        RefreshTokenResponseDTO response = new RefreshTokenResponseDTO();
        response.setAccessToken(accessToken);
        response.setTokenType("Bearer");
        response.setExpiresIn((long) accessTokenExpirationMinutes * 60);
        response.setUsuario(usuarioDTO);

        return new RefreshTokenRotationResult(response, newRawToken);
    }

	private UsuarioDTO mapToUsuarioDTO(Usuario usuario) {
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(usuario.getId());
		usuarioDTO.setUsername(usuario.getUsername());
		usuarioDTO.setEmail(usuario.getEmail());
		usuarioDTO.setEnabled(usuario.getEnabled());
		usuarioDTO.setFoto(usuario.getFoto());
		usuarioDTO.setEstadoKyc(usuario.getEstadoKyc());
		usuarioDTO.setUsername(usuario.getUsername());

		List<RoleDTO> roles = usuario.getRoles()
				.stream()
				.map(role -> new RoleDTO(role.getId(), role.getRol()))
				.toList();

		usuarioDTO.setRoles(roles);

		UserHealthProfile uh = usuario.getUsuarioKyc();

		if (Objects.nonNull(uh)) {
			usuarioDTO.setNivelActividadFisica(uh.getNivelActividadFisica());
			usuarioDTO.setWeightKg(uh.getWeightKg());
			usuarioDTO.setHeightM(uh.getHeightM());
			usuarioDTO.setBmi(uh.getBmi());
			usuarioDTO.setNombreCompleto(uh.getFullName());
		}
		return usuarioDTO;
	}

    private RefreshToken buildRefreshToken(Usuario usuario, String tokenHash) {
        LocalDateTime now = LocalDateTime.now();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setUser(usuario);
        refreshToken.setStatus(TokenStatusEnum.ACTIVE);
        refreshToken.setCreatedAt(now);
        refreshToken.setExpiration(now.plusDays(refreshTokenExpirationDays));
        return refreshToken;
    }

    private String generateRefreshTokenValue() {
        byte[] tokenBytes = new byte[TOKEN_BYTE_LENGTH];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    private String hashToken(String rawToken) {
    	 try {
    	        MessageDigest digest = MessageDigest.getInstance("SHA-256");

    	        byte[] hash = digest.digest(
    	            rawToken.getBytes(StandardCharsets.UTF_8)
    	        );

    	        return HexFormat.of().formatHex(hash);

    	    } catch (NoSuchAlgorithmException e) {
    	        throw new IllegalStateException(
    	            "Algoritmo SHA-256 no disponible", e
    	        );
    	    }
    }
}
