package edu.co.udes.spafeu.web.app.service.entity.impl;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.AcademicProgramDTO;
import edu.co.udes.spafeu.web.app.models.dto.CampusDTO;
import edu.co.udes.spafeu.web.app.models.entities.Campus;
import edu.co.udes.spafeu.web.app.models.entities.CampusProgram;
import edu.co.udes.spafeu.web.app.repositories.CampusRepository;
import edu.co.udes.spafeu.web.app.service.entity.CampusService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampusServiceImpl implements CampusService {

    private final CampusRepository campusRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CampusDTO> findAllCampusesWithPrograms() throws SpafeuException {
        List<Campus> campuses = campusRepository.findAllWithAcademicPrograms();
        
        campuses.forEach(campus -> {
            campus.setCampusPrograms(
                    campus.getCampusPrograms().stream()
                            .sorted(Comparator.comparing(
                                    cp -> cp.getProgram() != null ? cp.getProgram().getName() : null,
                                    Comparator.nullsFirst(String::compareTo)
                            ))
                            .toList()
            );
        });
        
        return campuses.stream()
                .map(campus -> {
                    CampusDTO dto = modelMapper.map(campus, CampusDTO.class);

                    List<AcademicProgramDTO> programs = campus.getCampusPrograms()
                            .stream()
                            .map(CampusProgram::getProgram)
                            .filter(Objects::nonNull)
                            .map(p -> modelMapper.map(p, AcademicProgramDTO.class))
                            .toList();

                    dto.setAcademicPrograms(programs);
                    return dto;
                })
                .toList();
    }
}
