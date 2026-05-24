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
@Table(name = "MOBILE_APPLICATION", uniqueConstraints = {@UniqueConstraint(columnNames = "NAME", name = "UQ_MOBILE_APPLICATION_NAME")})
@Getter
@Setter
public class MobileApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_APPLICATION")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;
}