package edu.co.udes.spafeu.web.app.models.entities;

import java.util.List;

import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.models.enums.converter.NivelActividadFisicaEnumConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
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

    @Convert(converter = NivelActividadFisicaEnumConverter.class)
    @Column(name = "NIVEL_ACTIVIDAD_FISICA", nullable = false, length = 15)
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