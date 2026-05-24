package edu.co.udes.spafeu.web.app.models.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import edu.co.udes.spafeu.web.app.models.enums.EstadoKycEnum;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.models.enums.SexEnum;
import edu.co.udes.spafeu.web.app.models.enums.converter.NivelActividadFisicaEnumConverter;
import edu.co.udes.spafeu.web.app.models.enums.converter.SexEnumConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USER_HEALTH_PROFILE", uniqueConstraints = { @UniqueConstraint(columnNames = "ID_USERS")})
@Getter
@Setter
@NoArgsConstructor
public class UserHealthProfile {

    @Id
    @Column(name = "ID_USERS", updatable = false, nullable = false)
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USERS", nullable = false)
    private Usuario user;

    @Column(name = "FULL_NAME", length = 100, nullable = false)
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
        @JoinColumn(name = "ID_CAMPUS", referencedColumnName = "ID_CAMPUS",nullable = false),
        @JoinColumn(name = "ID_PROGRAM", referencedColumnName = "ID_PROGRAM",nullable = false)
    })
    private CampusProgram campusProgram;

    @Column(name = "SEMESTER", nullable = false)
    private Integer semester;

    @Column(name = "BIRTH_DATE", nullable = false)
    private LocalDate birthDate;

    @Column(name = "SEX", length = 1, nullable = false)
    @Convert(converter = SexEnumConverter.class)
    private SexEnum sex;

    @Column(name = "WEIGHT_KG", precision = 5, scale = 2, nullable = false)
    private BigDecimal weightKg;

    @Column(name = "HEIGHT_M", precision = 4, scale = 2, nullable = false)
    private BigDecimal heightM;
    
    @Column(name = "BMI", precision = 4, scale = 2, nullable = false)
    private BigDecimal bmi;

    @Convert(converter = NivelActividadFisicaEnumConverter.class)
    @Column(name = "NIVEL_ACTIVIDAD_FISICA", length = 10, nullable = true)
    private NivelActividadFisicaEnum nivelActividadFisica;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_KYC", nullable = false, length = 20)
    private EstadoKycEnum estadoKyc;
    
    public void calculateBmi() {
        if (weightKg != null && heightM != null) {
            this.bmi = weightKg.divide(
                    heightM.multiply(heightM),
                    2,
                    RoundingMode.HALF_UP
            );
        }
    }
    
    @PrePersist
    @PreUpdate
    private void prePersist() {
        calculateBmi();
    }
}