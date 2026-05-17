package edu.co.udes.spafeu.web.app.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CAMPUS_PROGRAM")
@Getter
@Setter
@NoArgsConstructor
public class CampusProgram {

    @EmbeddedId
    private CampusProgramId id;

    @MapsId("campusId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CAMPUS", referencedColumnName = "ID_CAMPUS", nullable = false)
    private Campus campus;

    @MapsId("programId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROGRAM", referencedColumnName = "ID_PROGRAM", nullable = false)
    private AcademicProgram program;

    @Column(name = "ACTIVE")
    private Boolean active;

    public CampusProgram(Campus campus, AcademicProgram program) {
        this.campus = campus;
        this.program = program;
        this.id = new CampusProgramId(campus.getId(), program.getId());
    }
}