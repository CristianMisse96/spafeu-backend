package edu.co.udes.spafeu.web.app.models.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "QUESTIONNAIRE")
@Getter
@Setter
public class Questionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_QUESTIONNAIRE")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    
    @OneToMany(mappedBy = "questionnaire", fetch = FetchType.LAZY)
    private List<Question> questions;
}
