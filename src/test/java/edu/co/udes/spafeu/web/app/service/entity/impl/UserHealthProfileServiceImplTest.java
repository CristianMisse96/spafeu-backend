package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UserAnswerDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserHealthProfileDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.entities.CampusProgram;
import edu.co.udes.spafeu.web.app.models.entities.CampusProgramId;
import edu.co.udes.spafeu.web.app.models.entities.Option;
import edu.co.udes.spafeu.web.app.models.entities.Question;
import edu.co.udes.spafeu.web.app.models.entities.UserHealthProfile;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.EstadoKycEnum;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.models.enums.SexEnum;
import edu.co.udes.spafeu.web.app.repositories.CampusProgramRepository;
import edu.co.udes.spafeu.web.app.repositories.OptionRepository;
import edu.co.udes.spafeu.web.app.repositories.QuestionRepository;
import edu.co.udes.spafeu.web.app.repositories.UserAnswerRepository;
import edu.co.udes.spafeu.web.app.repositories.UserHealthProfileRepository;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import lombok.extern.log4j.Log4j2;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UserHealthProfileServiceImpl")
@Log4j2
class UserHealthProfileServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UserHealthProfileRepository userHealthProfileRepository;

    @Mock
    private UserAnswerRepository userAnswerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private CampusProgramRepository campusProgramRepository;

    @InjectMocks
    private UserHealthProfileServiceImpl userHealthProfileService;

    private Usuario user;
    private UserHealthProfileDTO profileDTO;
    private UserHealthProfile profileEntity;
    private UsuarioDTO usuarioResponse;
    private CampusProgram campusProgram;
    private static final Long USER_ID = 1L;
    private static final Long CAMPUS_ID = 1L;
    private static final Long PROGRAM_ID = 1L;

    @BeforeEach
    void setUp() {
        user = new Usuario();
        user.setId(USER_ID);

        campusProgram = new CampusProgram();
        campusProgram.setId(new CampusProgramId(CAMPUS_ID, PROGRAM_ID));

        profileDTO = new UserHealthProfileDTO();
        profileDTO.setFullName("Juan Perez");
        profileDTO.setCampusId(CAMPUS_ID);
        profileDTO.setProgramId(PROGRAM_ID);
        profileDTO.setSemester(3);
        profileDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        profileDTO.setSex(SexEnum.MALE);
        profileDTO.setWeightKg(new BigDecimal("70.50"));
        profileDTO.setHeightM(new BigDecimal("1.75"));

        profileEntity = new UserHealthProfile();
        profileEntity.setFullName("Juan Perez");
        profileEntity.setSemester(3);
        profileEntity.setBirthDate(LocalDate.of(2000, 1, 1));
        profileEntity.setSex(SexEnum.MALE);
        profileEntity.setWeightKg(new BigDecimal("70.50"));
        profileEntity.setHeightM(new BigDecimal("1.75"));
        profileEntity.setCampusProgram(campusProgram);

        usuarioResponse = new UsuarioDTO();
        usuarioResponse.setId(USER_ID);
        usuarioResponse.setEstadoKyc(EstadoKycEnum.COMPLETO);
    }

    @Nested
    @DisplayName("registerHealthProfile()")
    class RegisterHealthProfile {

        @Test
        @DisplayName("Debe registrar el perfil de salud y las respuestas exitosamente")
        void debeRegistrarPerfilYRespuestasExitosamente() throws SpafeuException {
            when(usuarioRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userHealthProfileRepository.existsByUserId(USER_ID)).thenReturn(false);
            when(campusProgramRepository.findByCampusIdAndProgramId(CAMPUS_ID, PROGRAM_ID))
                    .thenReturn(Optional.of(campusProgram));
            doAnswer(invocation -> invocation.getArgument(0))
                    .when(userHealthProfileRepository).save(any(UserHealthProfile.class));

            Question question = new Question();
            question.setId(1L);
            when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

            Option option = new Option();
            option.setId(1L);
            when(optionRepository.findById(1L)).thenReturn(Optional.of(option));

            UserAnswerDTO answerDTO = new UserAnswerDTO();
            answerDTO.setQuestionId(1L);
            answerDTO.setOptionId(1L);
            profileDTO.setAnswers(List.of(answerDTO));

            UsuarioDTO resultado = userHealthProfileService.registerHealthProfile(profileDTO, USER_ID);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(USER_ID);
            assertThat(resultado.getEstadoKyc()).isEqualTo(EstadoKycEnum.COMPLETO);
            verify(usuarioRepository, times(1)).findById(USER_ID);
            verify(userHealthProfileRepository, times(1)).existsByUserId(USER_ID);
            verify(userHealthProfileRepository, times(1)).save(any(UserHealthProfile.class));
            verify(userAnswerRepository, times(1)).saveAll(anyList());
            log.info("Test registro exitoso: perfil y respuestas guardados correctamente");
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el usuario no existe")
        void debeLanzarExcepcionCuandoUsuarioNoExiste() {
            when(usuarioRepository.findById(USER_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                userHealthProfileService.registerHealthProfile(profileDTO, USER_ID)
            ).isInstanceOf(SpafeuException.class);

            verify(usuarioRepository, times(1)).findById(USER_ID);
            log.info("Test usuario no existe: SpafeuException lanzada correctamente");
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el usuario ya tiene perfil de salud")
        void debeLanzarExcepcionCuandoYaTienePerfil() {
            when(usuarioRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userHealthProfileRepository.existsByUserId(USER_ID)).thenReturn(true);

            assertThatThrownBy(() ->
                userHealthProfileService.registerHealthProfile(profileDTO, USER_ID)
            ).isInstanceOf(SpafeuException.class);

            verify(userHealthProfileRepository, times(1)).existsByUserId(USER_ID);
            log.info("Test perfil ya existe: SpafeuException lanzada correctamente");
        }

        @Test
        @DisplayName("Debe registrar el perfil sin respuestas cuando la lista de answers es null")
        void debeRegistrarPerfilSinRespuestasCuandoAnswersEsNull() throws SpafeuException {
            when(usuarioRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userHealthProfileRepository.existsByUserId(USER_ID)).thenReturn(false);
            when(campusProgramRepository.findByCampusIdAndProgramId(CAMPUS_ID, PROGRAM_ID))
                    .thenReturn(Optional.of(campusProgram));
            doAnswer(invocation -> invocation.getArgument(0))
                    .when(userHealthProfileRepository).save(any(UserHealthProfile.class));

            profileDTO.setAnswers(null);

            UsuarioDTO resultado = userHealthProfileService.registerHealthProfile(profileDTO, USER_ID);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getEstadoKyc()).isEqualTo(EstadoKycEnum.COMPLETO);
            verify(userAnswerRepository, times(0)).saveAll(anyList());
            log.info("Test sin respuestas: perfil registrado sin guardar respuestas");
        }

        @Test
        @DisplayName("Debe registrar el perfil sin respuestas cuando la lista de answers está vacía")
        void debeRegistrarPerfilSinRespuestasCuandoAnswersVacia() throws SpafeuException {
            when(usuarioRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userHealthProfileRepository.existsByUserId(USER_ID)).thenReturn(false);
            when(campusProgramRepository.findByCampusIdAndProgramId(CAMPUS_ID, PROGRAM_ID))
                    .thenReturn(Optional.of(campusProgram));
            doAnswer(invocation -> invocation.getArgument(0))
                    .when(userHealthProfileRepository).save(any(UserHealthProfile.class));

            profileDTO.setAnswers(List.of());

            UsuarioDTO resultado = userHealthProfileService.registerHealthProfile(profileDTO, USER_ID);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getEstadoKyc()).isEqualTo(EstadoKycEnum.COMPLETO);
            verify(userAnswerRepository, times(0)).saveAll(anyList());
            log.info("Test respuestas vacías: perfil registrado sin guardar respuestas");
        }

        @Test
        @DisplayName("Debe clasificar nivel de actividad física NIVEL_1 cuando no hay respuestas")
        void debeClasificarNivelActividadFisicaSinRespuestas() throws SpafeuException {
            when(usuarioRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userHealthProfileRepository.existsByUserId(USER_ID)).thenReturn(false);
            when(campusProgramRepository.findByCampusIdAndProgramId(CAMPUS_ID, PROGRAM_ID))
                    .thenReturn(Optional.of(campusProgram));
            doAnswer(invocation -> invocation.getArgument(0))
                    .when(userHealthProfileRepository).save(any(UserHealthProfile.class));

            profileDTO.setAnswers(null);

            UsuarioDTO resultado = userHealthProfileService.registerHealthProfile(profileDTO, USER_ID);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getNivelActividadFisica()).isEqualTo(NivelActividadFisicaEnum.NIVEL_1);
            log.info("Test clasificación sin respuestas: NIVEL_1 por defecto");
        }

        @Test
        @DisplayName("Debe clasificar nivel de actividad física NIVEL_4 cuando hay actividad vigorosa")
        void debeClasificarNivelActividadFisicaNivel4() throws SpafeuException {
            when(usuarioRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userHealthProfileRepository.existsByUserId(USER_ID)).thenReturn(false);
            when(campusProgramRepository.findByCampusIdAndProgramId(CAMPUS_ID, PROGRAM_ID))
                    .thenReturn(Optional.of(campusProgram));
            doAnswer(invocation -> invocation.getArgument(0))
                    .when(userHealthProfileRepository).save(any(UserHealthProfile.class));

            // Mock questions and options for answers
            Question question10 = new Question();
            question10.setId(10L);
            when(questionRepository.findById(10L)).thenReturn(Optional.of(question10));

            Question question11 = new Question();
            question11.setId(11L);
            when(questionRepository.findById(11L)).thenReturn(Optional.of(question11));

            Question question12 = new Question();
            question12.setId(12L);
            when(questionRepository.findById(12L)).thenReturn(Optional.of(question12));

            Question question13 = new Question();
            question13.setId(13L);
            when(questionRepository.findById(13L)).thenReturn(Optional.of(question13));

            Option option6 = new Option();
            option6.setId(6L);
            when(optionRepository.findById(6L)).thenReturn(Optional.of(option6));

            Option option9 = new Option();
            option9.setId(9L);
            when(optionRepository.findById(9L)).thenReturn(Optional.of(option9));

            Option option13 = new Option();
            option13.setId(13L);
            when(optionRepository.findById(13L)).thenReturn(Optional.of(option13));

            Option option16 = new Option();
            option16.setId(16L);
            when(optionRepository.findById(16L)).thenReturn(Optional.of(option16));

            // Pregunta 10: 5+ días (ID 6)
            UserAnswerDTO answer1 = new UserAnswerDTO();
            answer1.setQuestionId(10L);
            answer1.setOptionId(6L);

            // Pregunta 11: duración 30-60 min (ID 9)
            UserAnswerDTO answer2 = new UserAnswerDTO();
            answer2.setQuestionId(11L);
            answer2.setOptionId(9L);

            // Pregunta 12: actividad vigorosa (ID 13)
            UserAnswerDTO answer3 = new UserAnswerDTO();
            answer3.setQuestionId(12L);
            answer3.setOptionId(13L);

            // Pregunta 13: sedentario 6-8 horas (ID 16)
            UserAnswerDTO answer4 = new UserAnswerDTO();
            answer4.setQuestionId(13L);
            answer4.setOptionId(16L);

            profileDTO.setAnswers(List.of(answer1, answer2, answer3, answer4));

            UsuarioDTO resultado = userHealthProfileService.registerHealthProfile(profileDTO, USER_ID);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getNivelActividadFisica()).isEqualTo(NivelActividadFisicaEnum.NIVEL_4);
            log.info("Test clasificación NIVEL_4: actividad vigorosa detectada");
        }
    }
}
