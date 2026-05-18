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
@Table(name = "OPTION", uniqueConstraints = {@UniqueConstraint(columnNames = "LABEL", name = "UQ_OPTION_LABEL")})
@Getter 
@Setter
public class Option {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_OPTION")
    private Long id;

    @Column(name = "LABEL")
    private String label;
}
