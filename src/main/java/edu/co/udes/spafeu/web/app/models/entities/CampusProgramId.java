package edu.co.udes.spafeu.web.app.models.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CampusProgramId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "ID_CAMPUS")
    private Long campusId;

    @Column(name = "ID_PROGRAM")
    private Long programId;
}