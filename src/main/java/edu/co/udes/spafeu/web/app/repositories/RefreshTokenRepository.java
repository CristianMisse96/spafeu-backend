package edu.co.udes.spafeu.web.app.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.co.udes.spafeu.web.app.models.entities.RefreshToken;
import edu.co.udes.spafeu.web.app.models.enums.TokenStatusEnum;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Repositorio de la entidad {@link RefreshToken}. Gestiona el acceso
 * a datos de los tokens de refresco persistentes, incluyendo búsqueda por hash y estado,
 * invalidación masiva por usuario y limpieza de tokens expirados.</p>
 *
 * @author SPAFEU Backend Team
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Busca un refresh token activo por el hash del token y su estado.
     * Se aplica bloqueo pesimista de escritura para prevenir condiciones de
     * carrera cuando dos peticiones concurrentes intentan usar el mismo token.
     *
     * @param tokenHash hash SHA-256 del token original
     * @param status    estado del token
     * @return el {@link RefreshToken} encontrado, o vacío si no existe
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RefreshToken> findByTokenHashAndStatus(String tokenHash, TokenStatusEnum status);

    /**
     * Invalida todos los tokens activos de un usuario actualizando su estado.
     *
     * @param userId identificador del usuario
     * @param status nuevo estado a aplicar (por ejemplo REVOKED)
     */
    @Modifying
    @Query("UPDATE RefreshToken r SET r.status = :status WHERE r.user.id = :userId AND r.status = 'ACTIVE'")
    void invalidateTokensByUser(@Param("userId") Long userId, @Param("status") TokenStatusEnum status);

    /**
     * Elimina todos los tokens cuya fecha de expiración sea anterior a la fecha indicada.
     * Útil para la limpieza programada de tokens expirados.
     *
     * @param date fecha límite de expiración
     */
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expiration < :date")
    void deleteByExpirationBefore(@Param("date") LocalDateTime date);
}
