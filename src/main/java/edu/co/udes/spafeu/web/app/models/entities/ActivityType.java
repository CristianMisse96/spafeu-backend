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
@Table(name = "ACTIVITY_TYPE", uniqueConstraints = {@UniqueConstraint(columnNames = "NAME", name = "UQ_ACTIVITY_TYPE_NAME")})
@Getter
@Setter
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ACTIVITY_TYPE")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
}