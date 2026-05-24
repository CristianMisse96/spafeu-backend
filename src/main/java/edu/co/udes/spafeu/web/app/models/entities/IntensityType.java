package edu.co.udes.spafeu.web.app.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "INTENSITY_TYPE", uniqueConstraints = {@UniqueConstraint(columnNames = "NAME", name = "UQ_INTENSITY_TYPE_NAME")})
@Getter
@Setter
@NoArgsConstructor
public class IntensityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INTENSITY_TYPE")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
}