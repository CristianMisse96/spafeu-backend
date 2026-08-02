package edu.co.udes.spafeu.web.app.models.entities;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PHYSICAL_ACTIVITY_PROTOCOL")
@Getter
@Setter
public class PhysicalActivityProtocol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROTOCOL")
    private Long id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "NIVEL_ACTIVIDAD_FISICA", columnDefinition = "nivel_actividad_fisica_type", nullable = false)
    private NivelActividadFisicaEnum nivelActividadFisica;

    @Column(name = "NAME", nullable = false, length = 150)
    private String name;
    
    @Column(name = "DESCRIPTION", nullable = false, length = 3000)
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DURATION")
    private DurationType suggestedDuration;
    
    @OneToMany(mappedBy = "protocol", cascade = {CascadeType.MERGE, CascadeType.PERSIST},orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProtocolObjective> objectives;

    @OneToMany(mappedBy = "protocol",cascade = {CascadeType.MERGE, CascadeType.PERSIST},orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Exercise> exercises;
}