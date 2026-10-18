package edu.co.udes.spafeu.web.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.co.udes.spafeu.web.app.models.entities.AccountActivationToken;
import edu.co.udes.spafeu.web.app.models.enums.TokenStatusEnum;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Repositorio de la entidad AccountActivationToken.</p>
 * @author cristian_misse
 */
@Repository
public interface AccountActivationTokenRepository extends JpaRepository<AccountActivationToken, Long> {
	
	 Optional<AccountActivationToken> findByTokenAndStatus(String token, TokenStatusEnum status);

	 @Modifying
	 @Query("UPDATE AccountActivationToken t SET t.status = :status WHERE t.user.id = :userId AND t.status = 'ACTIVE'")
	 void invalidateTokensByUser(@Param("userId") Long userId, @Param("status") TokenStatusEnum status);
}
