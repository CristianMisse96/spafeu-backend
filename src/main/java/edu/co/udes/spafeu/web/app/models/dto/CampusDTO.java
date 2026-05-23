package edu.co.udes.spafeu.web.app.models.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CampusDTO {

    private Long id;
    private String name;
    private List<AcademicProgramDTO> academicPrograms;
}
