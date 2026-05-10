package edu.co.udes.spafeu.web.app.models.entities;

import java.time.LocalDateTime;

import edu.co.udes.spafeu.web.app.models.enums.TokenStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "password_reset_tokens", uniqueConstraints = {
	    @UniqueConstraint(columnNames = "TOKEN", name = "PASSWORD_RESET_TOKEN_UNIQUE")
	    })
public class PasswordResetToken {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 	@Column(name = "ID_PASSWORD_RESET_TOKEN")
	    private Long id;

	    @Column(name = "TOKEN", nullable = false, length = 64)
	    private String token;

	    @ManyToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private Usuario user;

	    @Column(name = "EXPIRATION",nullable = false)
	    private LocalDateTime expiration;

	    @Enumerated(EnumType.STRING)
	    @Column(name="STATUS", nullable = false)
	    private TokenStatusEnum status;
}
