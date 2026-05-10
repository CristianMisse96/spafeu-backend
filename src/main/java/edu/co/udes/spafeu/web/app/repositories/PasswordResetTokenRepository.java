package edu.co.udes.spafeu.web.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.co.udes.spafeu.web.app.models.entities.PasswordResetToken;
import edu.co.udes.spafeu.web.app.models.enums.TokenStatusEnum;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	
	 Optional<PasswordResetToken> findByTokenAndStatus(String token, TokenStatusEnum status);

	 @Modifying
	 @Query("UPDATE PasswordResetToken t SET t.status = :status WHERE t.user.id = :userId AND t.status = 'ACTIVE'")
	 void invalidateTokensByUser(@Param("userId") Long userId, @Param("status") TokenStatusEnum status);
}
