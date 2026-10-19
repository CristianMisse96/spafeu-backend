package edu.co.udes.spafeu.web.app.models.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Entidad JPA que registra el histórico de cambios de nivel de actividad física de un usuario.
 * </p>
 *
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "USER_LEVEL_HISTORY")
public class UserLevelHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USER_LEVEL_HISTORY", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USERS", nullable = false)
    private Usuario user;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "NIVEL_ANTERIOR", columnDefinition = "nivel_actividad_fisica_type")
    private NivelActividadFisicaEnum nivelAnterior;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "NIVEL_NUEVO", columnDefinition = "nivel_actividad_fisica_type", nullable = false)
    private NivelActividadFisicaEnum nivelNuevo;

    @Column(name = "FECHA_CAMBIO", nullable = false)
    private LocalDateTime fechaCambio;

    @Column(name = "OBSERVACION", length = 500)
    private String observacion;
}
