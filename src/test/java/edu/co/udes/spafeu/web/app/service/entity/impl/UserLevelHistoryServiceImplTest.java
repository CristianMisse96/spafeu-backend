package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UserLevelHistoryDTO;
import edu.co.udes.spafeu.web.app.models.entities.UserLevelHistory;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.repositories.UserLevelHistoryRepository;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UserLevelHistoryServiceImpl")
@Log4j2
class UserLevelHistoryServiceImplTest {

    @Mock
    private UserLevelHistoryRepository userLevelHistoryRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserLevelHistoryServiceImpl userLevelHistoryService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("usuario.test");
    }

    private UserLevelHistoryDTO crearDtoEsperado(Long id, Long userId, NivelActividadFisicaEnum nivelNuevo) {
        UserLevelHistoryDTO dto = new UserLevelHistoryDTO();
        dto.setId(id);
        dto.setUserId(userId);
        dto.setNivelNuevo(nivelNuevo);
        return dto;
    }

    @Nested
    @DisplayName("registrarNivelInicial(Long, NivelActividadFisicaEnum)")
    class RegistrarNivelInicial {

        @Test
        @DisplayName("Debe crear registro inicial con nivel anterior nulo, observación y fecha correctas")
        void debeCrearRegistroInicialCorrectamente() throws SpafeuException {
            UserLevelHistoryDTO dtoEsperado = crearDtoEsperado(10L, 1L, NivelActividadFisicaEnum.NIVEL_2);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(userLevelHistoryRepository.save(any(UserLevelHistory.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(modelMapper.map(any(UserLevelHistory.class), eq(UserLevelHistoryDTO.class))).thenReturn(dtoEsperado);

            UserLevelHistoryDTO resultado = userLevelHistoryService
                    .registrarNivelInicial(1L, NivelActividadFisicaEnum.NIVEL_2);

            ArgumentCaptor<UserLevelHistory> captor = ArgumentCaptor.forClass(UserLevelHistory.class);
            verify(userLevelHistoryRepository, times(1)).save(captor.capture());
            UserLevelHistory guardado = captor.getValue();

            assertThat(guardado).isNotNull();
            assertThat(guardado.getUser()).isEqualTo(usuario);
            assertThat(guardado.getNivelAnterior()).isNull();
            assertThat(guardado.getNivelNuevo()).isEqualTo(NivelActividadFisicaEnum.NIVEL_2);
            assertThat(guardado.getFechaCambio()).isNotNull();
            assertThat(guardado.getObservacion()).isEqualTo("Nivel asignado en onboarding");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(10L);
            assertThat(resultado.getUserId()).isEqualTo(1L);
            assertThat(resultado.getNivelNuevo()).isEqualTo(NivelActividadFisicaEnum.NIVEL_2);

            verify(usuarioRepository, times(1)).findById(1L);
            log.info("Test registrarNivelInicial: registro inicial creado correctamente para userId={}", resultado.getUserId());
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el usuario no existe")
        void debeLanzarExcepcionCuandoUsuarioNoExiste() {
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> userLevelHistoryService.registrarNivelInicial(99L, NivelActividadFisicaEnum.NIVEL_2));

            assertThat(ex.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.ERROR_USUARIO_NO_EXISTE);
            verify(usuarioRepository, times(1)).findById(99L);
            verify(userLevelHistoryRepository, never()).save(any());
            log.info("Test registrarNivelInicial sin usuario: lanza SpafeuException con mensaje='{}'", ex.getMensaje());
        }
    }

    @Nested
    @DisplayName("registrarCambioNivel(Long, NivelActividadFisicaEnum, NivelActividadFisicaEnum, String)")
    class RegistrarCambioNivel {

        @Test
        @DisplayName("Debe crear registro de cambio de nivel con datos correctos")
        void debeCrearRegistroDeCambioCorrectamente() throws SpafeuException {
            String observacion = "Subió de nivel por completar protocolo";
            UserLevelHistoryDTO dtoEsperado = crearDtoEsperado(20L, 1L, NivelActividadFisicaEnum.NIVEL_2);
            dtoEsperado.setNivelAnterior(NivelActividadFisicaEnum.NIVEL_1);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(userLevelHistoryRepository.save(any(UserLevelHistory.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(modelMapper.map(any(UserLevelHistory.class), eq(UserLevelHistoryDTO.class))).thenReturn(dtoEsperado);

            UserLevelHistoryDTO resultado = userLevelHistoryService
                    .registrarCambioNivel(1L, NivelActividadFisicaEnum.NIVEL_1,
                            NivelActividadFisicaEnum.NIVEL_2, observacion);

            ArgumentCaptor<UserLevelHistory> captor = ArgumentCaptor.forClass(UserLevelHistory.class);
            verify(userLevelHistoryRepository, times(1)).save(captor.capture());
            UserLevelHistory guardado = captor.getValue();

            assertThat(guardado).isNotNull();
            assertThat(guardado.getUser()).isEqualTo(usuario);
            assertThat(guardado.getNivelAnterior()).isEqualTo(NivelActividadFisicaEnum.NIVEL_1);
            assertThat(guardado.getNivelNuevo()).isEqualTo(NivelActividadFisicaEnum.NIVEL_2);
            assertThat(guardado.getFechaCambio()).isNotNull();
            assertThat(guardado.getObservacion()).isEqualTo(observacion);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(20L);
            assertThat(resultado.getUserId()).isEqualTo(1L);

            verify(usuarioRepository, times(1)).findById(1L);
            log.info("Test registrarCambioNivel: cambio {} -> {} registrado correctamente",
                    NivelActividadFisicaEnum.NIVEL_1, NivelActividadFisicaEnum.NIVEL_2);
        }

        @Test
        @DisplayName("Debe lanzar SpafeuException cuando el usuario no existe")
        void debeLanzarExcepcionCuandoUsuarioNoExiste() {
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            SpafeuException ex = assertThrows(SpafeuException.class,
                    () -> userLevelHistoryService.registrarCambioNivel(99L,
                            NivelActividadFisicaEnum.NIVEL_1, NivelActividadFisicaEnum.NIVEL_2,
                            "Observación"));

            assertThat(ex.getMensaje()).isEqualTo(MensajesKeyConstantesUtil.ERROR_USUARIO_NO_EXISTE);
            verify(usuarioRepository, times(1)).findById(99L);
            verify(userLevelHistoryRepository, never()).save(any());
            log.info("Test registrarCambioNivel sin usuario: lanza SpafeuException con mensaje='{}'", ex.getMensaje());
        }
    }

    @Nested
    @DisplayName("findByUserId(Long)")
    class FindByUserId {

        @Test
        @DisplayName("Debe retornar lista ordenada descendente mapeando userId correctamente")
        void debeRetornarListaOrdenadaDescendente() throws SpafeuException {
            Usuario usuario2 = new Usuario();
            usuario2.setId(2L);

            UserLevelHistory registro1 = new UserLevelHistory();
            registro1.setId(1L);
            registro1.setUser(usuario);
            registro1.setNivelNuevo(NivelActividadFisicaEnum.NIVEL_2);
            registro1.setFechaCambio(LocalDateTime.now().minusDays(1));

            UserLevelHistory registro2 = new UserLevelHistory();
            registro2.setId(2L);
            registro2.setUser(usuario2);
            registro2.setNivelNuevo(NivelActividadFisicaEnum.NIVEL_3);
            registro2.setFechaCambio(LocalDateTime.now());

            List<UserLevelHistory> registros = List.of(registro2, registro1);

            UserLevelHistoryDTO dto1 = crearDtoEsperado(1L, 1L, NivelActividadFisicaEnum.NIVEL_2);
            UserLevelHistoryDTO dto2 = crearDtoEsperado(2L, 2L, NivelActividadFisicaEnum.NIVEL_3);

            when(userLevelHistoryRepository.findByUserIdOrderByFechaCambioDesc(1L)).thenReturn(registros);
            when(modelMapper.map(registro1, UserLevelHistoryDTO.class)).thenReturn(dto1);
            when(modelMapper.map(registro2, UserLevelHistoryDTO.class)).thenReturn(dto2);

            List<UserLevelHistoryDTO> resultado = userLevelHistoryService.findByUserId(1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getId()).isEqualTo(2L);
            assertThat(resultado.get(0).getUserId()).isEqualTo(2L);
            assertThat(resultado.get(0).getNivelNuevo()).isEqualTo(NivelActividadFisicaEnum.NIVEL_3);
            assertThat(resultado.get(1).getId()).isEqualTo(1L);
            assertThat(resultado.get(1).getUserId()).isEqualTo(1L);

            verify(userLevelHistoryRepository, times(1)).findByUserIdOrderByFechaCambioDesc(1L);
            log.info("Test findByUserId: retornados {} registros ordenados descendente", resultado.size());
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay registros")
        void debeRetornarListaVacia() throws SpafeuException {
            when(userLevelHistoryRepository.findByUserIdOrderByFechaCambioDesc(1L)).thenReturn(List.of());

            List<UserLevelHistoryDTO> resultado = userLevelHistoryService.findByUserId(1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();
            verify(userLevelHistoryRepository, times(1)).findByUserIdOrderByFechaCambioDesc(1L);
            log.info("Test findByUserId sin datos: retorna lista vacía");
        }

        @Test
        @DisplayName("Debe mapear userId como null cuando el registro no tiene usuario asociado")
        void debeMapearUserIdNuloCuandoUsuarioEsNull() throws SpafeuException {
            UserLevelHistory registroSinUsuario = new UserLevelHistory();
            registroSinUsuario.setId(3L);
            registroSinUsuario.setUser(null);
            registroSinUsuario.setNivelNuevo(NivelActividadFisicaEnum.NIVEL_1);
            registroSinUsuario.setFechaCambio(LocalDateTime.now());

            UserLevelHistoryDTO dtoSinUsuario = crearDtoEsperado(3L, null, NivelActividadFisicaEnum.NIVEL_1);

            when(userLevelHistoryRepository.findByUserIdOrderByFechaCambioDesc(1L))
                    .thenReturn(List.of(registroSinUsuario));
            when(modelMapper.map(registroSinUsuario, UserLevelHistoryDTO.class)).thenReturn(dtoSinUsuario);

            List<UserLevelHistoryDTO> resultado = userLevelHistoryService.findByUserId(1L);

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getId()).isEqualTo(3L);
            assertThat(resultado.get(0).getUserId()).isNull();

            verify(userLevelHistoryRepository, times(1)).findByUserIdOrderByFechaCambioDesc(1L);
            log.info("Test findByUserId sin usuario: userId mapeado como null");
        }
    }
}
