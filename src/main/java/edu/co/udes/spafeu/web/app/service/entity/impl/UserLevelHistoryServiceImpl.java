package edu.co.udes.spafeu.web.app.service.entity.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UserLevelHistoryDTO;
import edu.co.udes.spafeu.web.app.models.entities.UserLevelHistory;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.repositories.UserLevelHistoryRepository;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.service.entity.UserLevelHistoryService;
import edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Implementación de {@link UserLevelHistoryService}
 * con la lógica de negocio para el histórico de niveles de actividad física.
 * </p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
@Service
@RequiredArgsConstructor
public class UserLevelHistoryServiceImpl implements UserLevelHistoryService {

    private static final String OBSERVACION_NIVEL_INICIAL = "Nivel asignado en onboarding";

    private final UserLevelHistoryRepository userLevelHistoryRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserLevelHistoryDTO registrarNivelInicial(Long userId, NivelActividadFisicaEnum nivelNuevo) throws SpafeuException {
        Usuario usuario = obtenerUsuario(userId);

        UserLevelHistory registro = new UserLevelHistory();
        registro.setUser(usuario);
        registro.setNivelAnterior(null);
        registro.setNivelNuevo(nivelNuevo);
        registro.setFechaCambio(LocalDateTime.now());
        registro.setObservacion(OBSERVACION_NIVEL_INICIAL);

        UserLevelHistory saved = userLevelHistoryRepository.save(registro);
        return toDto(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserLevelHistoryDTO registrarCambioNivel(Long userId, NivelActividadFisicaEnum nivelAnterior,
            NivelActividadFisicaEnum nivelNuevo, String observacion) throws SpafeuException {
        Usuario usuario = obtenerUsuario(userId);

        UserLevelHistory registro = new UserLevelHistory();
        registro.setUser(usuario);
        registro.setNivelAnterior(nivelAnterior);
        registro.setNivelNuevo(nivelNuevo);
        registro.setFechaCambio(LocalDateTime.now());
        registro.setObservacion(observacion);

        UserLevelHistory saved = userLevelHistoryRepository.save(registro);
        return toDto(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserLevelHistoryDTO> findByUserId(Long userId) throws SpafeuException {
        return userLevelHistoryRepository.findByUserIdOrderByFechaCambioDesc(userId).stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Obtiene un usuario por su identificador único.
     *
     * @param userId identificador del usuario
     * @return entidad {@link Usuario} encontrada
     * @throws SpafeuException si el usuario no existe
     */
    private Usuario obtenerUsuario(Long userId) throws SpafeuException {
        return usuarioRepository.findById(userId)
                .orElseThrow(() -> new SpafeuException(MensajesKeyConstantesUtil.ERROR_USUARIO_NO_EXISTE));
    }

    /**
     * Convierte una entidad {@link UserLevelHistory} a su DTO asociado,
     * asignando manualmente el identificador del usuario.
     *
     * @param entity entidad de histórico de niveles
     * @return {@link UserLevelHistoryDTO} con los datos mapeados
     */
    private UserLevelHistoryDTO toDto(UserLevelHistory entity) {
        UserLevelHistoryDTO dto = modelMapper.map(entity, UserLevelHistoryDTO.class);
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
        return dto;
    }
}
