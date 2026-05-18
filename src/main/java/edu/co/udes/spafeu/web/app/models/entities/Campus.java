package edu.co.udes.spafeu.web.app.models.entities;

import java.util.List;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CAMPUS")
@Getter
@Setter
@NoArgsConstructor
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CAMPUS")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

    @OneToMany(mappedBy = "campus")
    private List<CampusProgram> campusPrograms;
}