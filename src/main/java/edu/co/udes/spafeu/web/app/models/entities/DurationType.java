package edu.co.udes.spafeu.web.app.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DURATION_TYPE", uniqueConstraints = {@UniqueConstraint(columnNames = "DESCRIPTION", name = "UQ_DURANTION_TYPE_DESCRIPTION")})
@Getter
@Setter
public class DurationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DURATION")
    private Long id;

    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;
}