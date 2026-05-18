package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import edu.co.udes.spafeu.web.app.models.dto.AcademicProgramDTO;
import edu.co.udes.spafeu.web.app.models.dto.CampusDTO;
import edu.co.udes.spafeu.web.app.models.entities.AcademicProgram;
import edu.co.udes.spafeu.web.app.models.entities.Campus;
import edu.co.udes.spafeu.web.app.models.entities.CampusProgram;
import edu.co.udes.spafeu.web.app.repositories.CampusRepository;
import lombok.extern.log4j.Log4j2;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de CampusServiceImpl")
@Log4j2
class CampusServiceImplTest {

    @Mock
    private CampusRepository campusRepository;

    private CampusServiceImpl campusService;
    private ModelMapper modelMapper;
    private Campus campus1;
    private Campus campus2;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Campus.class, CampusDTO.class)
                .addMappings(mapper -> mapper.skip(CampusDTO::setAcademicPrograms));
        modelMapper.createTypeMap(AcademicProgram.class, AcademicProgramDTO.class);

        campusService = new CampusServiceImpl(campusRepository, modelMapper);

        campus1 = new Campus();
        campus1.setId(1L);
        campus1.setName("Campus Principal");

        campus2 = new Campus();
        campus2.setId(2L);
        campus2.setName("Campus Norte");
    }

    @Nested
    @DisplayName("findAllCampusesWithPrograms()")
    class FindAllCampusesWithPrograms {

        @Test
        @DisplayName("Debe retornar lista de campus con programas ordenados alfabéticamente cuando hay datos")
        void debeRetornarCampusConProgramasOrdenados() {
            AcademicProgram program1 = new AcademicProgram();
            program1.setId(10L);
            program1.setName("Ingeniería de Sistemas");

            AcademicProgram program2 = new AcademicProgram();
            program2.setId(20L);
            program2.setName("Administración de Empresas");

            CampusProgram cp1 = new CampusProgram(campus1, program1);
            CampusProgram cp2 = new CampusProgram(campus1, program2);
            campus1.setCampusPrograms(List.of(cp1, cp2));

            AcademicProgram program3 = new AcademicProgram();
            program3.setId(30L);
            program3.setName("Contaduría Pública");

            CampusProgram cp3 = new CampusProgram(campus2, program3);
            campus2.setCampusPrograms(List.of(cp3));

            List<Campus> campuses = List.of(campus1, campus2);

            when(campusRepository.findAllWithAcademicPrograms()).thenReturn(campuses);

            List<CampusDTO> resultado = campusService.findAllCampusesWithPrograms();

            assertThat(resultado).isNotNull().hasSize(2);

            CampusDTO dto1 = resultado.stream().filter(c -> c.getId().equals(1L)).findFirst().orElseThrow();
            assertThat(dto1.getName()).isEqualTo("Campus Principal");
            assertThat(dto1.getAcademicPrograms()).hasSize(2);
            assertThat(dto1.getAcademicPrograms())
                    .extracting(AcademicProgramDTO::getName)
                    .containsExactly(
                            "Administración de Empresas",
                            "Ingeniería de Sistemas"
                    );

            CampusDTO dto2 = resultado.stream().filter(c -> c.getId().equals(2L)).findFirst().orElseThrow();
            assertThat(dto2.getName()).isEqualTo("Campus Norte");
            assertThat(dto2.getAcademicPrograms()).hasSize(1);
            assertThat(dto2.getAcademicPrograms())
                    .extracting(AcademicProgramDTO::getName)
                    .containsExactly("Contaduría Pública");

            verify(campusRepository, times(1)).findAllWithAcademicPrograms();
            log.info("Test findAllCampusesWithPrograms con datos: retorna {} campus", resultado.size());
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay campus")
        void debeRetornarListaVaciaCuandoNoHayDatos() {
            when(campusRepository.findAllWithAcademicPrograms()).thenReturn(List.of());

            List<CampusDTO> resultado = campusService.findAllCampusesWithPrograms();

            assertThat(resultado).isNotNull().isEmpty();
            verify(campusRepository, times(1)).findAllWithAcademicPrograms();
            log.info("Test findAllCampusesWithPrograms sin datos: retorna lista vacía");
        }

        @Test
        @DisplayName("Debe retornar campus sin programas cuando campusPrograms está vacío")
        void debeRetornarCampusSinProgramas() {
            campus1.setCampusPrograms(List.of());
            when(campusRepository.findAllWithAcademicPrograms()).thenReturn(List.of(campus1));

            List<CampusDTO> resultado = campusService.findAllCampusesWithPrograms();

            assertThat(resultado).isNotNull().hasSize(1);
            CampusDTO dto = resultado.get(0);
            assertThat(dto.getAcademicPrograms()).isEmpty();
            verify(campusRepository, times(1)).findAllWithAcademicPrograms();
            log.info("Test campus sin programas: retorna campus con lista vacía de programas");
        }

        @Test
        @DisplayName("Debe ordenar los programas alfabéticamente por nombre")
        void debeOrdenarProgramasAlfabeticamente() {
            AcademicProgram programZ = new AcademicProgram();
            programZ.setId(1L);
            programZ.setName("Zootecnia");

            AcademicProgram programA = new AcademicProgram();
            programA.setId(2L);
            programA.setName("Agronomía");

            AcademicProgram programM = new AcademicProgram();
            programM.setId(3L);
            programM.setName("Medicina");

            CampusProgram cpZ = new CampusProgram(campus1, programZ);
            CampusProgram cpA = new CampusProgram(campus1, programA);
            CampusProgram cpM = new CampusProgram(campus1, programM);
            campus1.setCampusPrograms(List.of(cpZ, cpA, cpM));

            when(campusRepository.findAllWithAcademicPrograms()).thenReturn(List.of(campus1));

            List<CampusDTO> resultado = campusService.findAllCampusesWithPrograms();

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getAcademicPrograms())
                    .extracting(AcademicProgramDTO::getName)
                    .containsExactly("Agronomía", "Medicina", "Zootecnia");
            log.info("Test orden alfabético: programas ordenados correctamente");
        }

        @Test
        @DisplayName("Debe filtrar programas nulos en campusPrograms")
        void debeFiltrarProgramasNulos() {
            AcademicProgram program1 = new AcademicProgram();
            program1.setId(10L);
            program1.setName("Ingeniería de Sistemas");

            CampusProgram cp1 = new CampusProgram(campus1, program1);
            CampusProgram cpNull = new CampusProgram(campus1, null);
            campus1.setCampusPrograms(List.of(cp1, cpNull));

            when(campusRepository.findAllWithAcademicPrograms()).thenReturn(List.of(campus1));

            List<CampusDTO> resultado = campusService.findAllCampusesWithPrograms();

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getAcademicPrograms()).hasSize(1);
            assertThat(resultado.get(0).getAcademicPrograms().get(0).getName())
                    .isEqualTo("Ingeniería de Sistemas");
            log.info("Test filtro de nulos: solo programas no nulos incluidos");
        }
    }
}
