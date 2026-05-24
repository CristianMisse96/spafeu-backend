package edu.co.udes.spafeu.web.app.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "EXERCISE")
@Getter
@Setter
@NoArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EXERCISE")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROTOCOL", nullable = false)
    private PhysicalActivityProtocol protocol;

    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @Column(name = "EXERCISE_DESCRIPTION", nullable = false, columnDefinition = "TEXT")
    private String descripcionEjercicio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ACTIVITY_TYPE", nullable = false)
    private ActivityType tipoActividadFisica;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_INTENSITY_TYPE", nullable = false)
    private IntensityType tipoIntensidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_DURATION", nullable = false)
    private DurationType duracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_APPLICATION")
    private MobileApplication application;

    @Column(name = "PHOTO_URL", length = 500)
    private String foto;
}