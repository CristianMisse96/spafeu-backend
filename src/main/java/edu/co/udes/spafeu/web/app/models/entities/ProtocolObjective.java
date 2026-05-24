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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PROTOCOL_OBJECTIVE", uniqueConstraints = {@UniqueConstraint(columnNames = "DESCRIPTION", name = "UQ_PROTOCOL_OBJECTIVE_DESCRIPTION")})
@Getter
@Setter
public class ProtocolObjective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROTOCOL")
    private PhysicalActivityProtocol protocol;

    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;
}