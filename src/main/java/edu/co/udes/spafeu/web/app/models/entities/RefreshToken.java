package edu.co.udes.spafeu.web.app.models.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import edu.co.udes.spafeu.web.app.models.enums.TokenStatusEnum;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Entidad JPA que representa un refresh token opaco persistente
 * asociado a un usuario. Almacena únicamente el hash SHA-256 del token original,
 * junto con su expiración, estado y trazas de auditoría.</p>
 *
 * @author SPAFEU Backend Team
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "refresh_tokens", uniqueConstraints = {
        @UniqueConstraint(columnNames = "TOKEN_HASH", name = "UQ_REFRESH_TOKEN_HASH")
})
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REFRESH_TOKEN", updatable = false, nullable = false)
    private Long id;

    @Column(name = "TOKEN_HASH", nullable = false, length = 64)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario user;

    @Column(name = "EXPIRATION", nullable = false)
    private LocalDateTime expiration;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private TokenStatusEnum status;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "USED_AT")
    private LocalDateTime usedAt;
}
