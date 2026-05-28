package edu.co.udes.spafeu.web.app.service.entity.impl;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UserExerciseSessionDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserProgressDashboardDTO;
import edu.co.udes.spafeu.web.app.models.entities.UserExerciseSession;
import edu.co.udes.spafeu.web.app.repositories.UserExerciseSessionRepository;
import edu.co.udes.spafeu.web.app.service.entity.UserExerciseSessionService;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Implementación del Service con lógica de negocio para la gestión de sesiones de ejercicio de usuario.</p>
 * @author SPAFEU
 */
@Service
@RequiredArgsConstructor
public class UserExerciseSessionServiceImpl implements UserExerciseSessionService {

    private final UserExerciseSessionRepository userExerciseSessionRepository;
    private final ModelMapper modelMapper;

    /**
     * Obtiene el dashboard de progreso del usuario autenticado.
     *
     * @param userId el identificador del usuario
     * @return DTO con el resumen de progreso y sesiones recientes
     * @throws SpafeuException si ocurre un error de negocio
     */
    @Override
    @Transactional(readOnly = true)
    public UserProgressDashboardDTO getUserProgressDashboard(Long userId) throws SpafeuException {
        UserProgressDashboardDTO dashboard = new UserProgressDashboardDTO();

        Long totalSesiones = userExerciseSessionRepository.countCompletedSessionsByUserId(userId);
        Integer totalMinutos = userExerciseSessionRepository.sumMinutesCompletedByUserId(userId);

        dashboard.setTotalSesiones(totalSesiones);
        dashboard.setTotalMinutos(totalMinutos);
        dashboard.setUltimaActividad(
                userExerciseSessionRepository.findLastActivityDateByUserId(userId).orElse(null));

        List<UserExerciseSession> recentSessions = userExerciseSessionRepository
                .findRecentCompletedSessionsByUserId(userId);

        List<UserExerciseSessionDTO> sesionesRecientes = recentSessions.stream()
                .limit(5)
                .map(this::mapToDto)
                .collect(Collectors.toList());

        dashboard.setSesionesRecientes(sesionesRecientes);

        return dashboard;
    }

    /**
     * Mapea una entidad {@link UserExerciseSession} a su DTO {@link UserExerciseSessionDTO},
     * incluyendo los campos anidados de {@code exercise} y {@code tipoActividadFisica} con null safety.
     *
     * @param session la sesión de ejercicio a mapear
     * @return el DTO mapeado
     */
    private UserExerciseSessionDTO mapToDto(UserExerciseSession session) {
        UserExerciseSessionDTO dto = modelMapper.map(session, UserExerciseSessionDTO.class);
        if (session.getExercise() != null) {
            dto.setExerciseId(session.getExercise().getId());
            dto.setExerciseName(session.getExercise().getName());
            dto.setExercisePhoto(session.getExercise().getFoto());
            if (session.getExercise().getTipoActividadFisica() != null) {
                dto.setActivityType(session.getExercise().getTipoActividadFisica().getName());
            }
        }
        return dto;
    }
}
