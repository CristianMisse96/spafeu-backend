package edu.co.udes.spafeu.web.app.service.entity.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseCompletionResultDTO;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseDTO;
import edu.co.udes.spafeu.web.app.models.entities.Exercise;
import edu.co.udes.spafeu.web.app.models.entities.UserExerciseSession;
import edu.co.udes.spafeu.web.app.models.entities.UserHealthProfile;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.repositories.ExerciseRepository;
import edu.co.udes.spafeu.web.app.repositories.UserExerciseSessionRepository;
import edu.co.udes.spafeu.web.app.repositories.UserHealthProfileRepository;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.service.entity.ExerciseService;
import edu.co.udes.spafeu.web.app.service.entity.UserLevelHistoryService;
import edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Implementación de la interfaz {@link ExerciseService}
 * con la lógica de negocio para la gestión de ejercicios.</p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserExerciseSessionRepository userExerciseSessionRepository;
    private final UserHealthProfileRepository userHealthProfileRepository;
    private final UsuarioRepository usuarioRepository;
    private final UserLevelHistoryService userLevelHistoryService;
    private final ModelMapper modelMapper;

    /**
     * Obtiene un ejercicio por su identificador unico incluyendo sus detalles
     * y valida que pertenezca al nivel de actividad fisica indicado.
     *
     * @param id el identificador del ejercicio
     * @param nivelActividadFisica el nivel de actividad fisica esperado
     * @return {@link ExerciseDTO} con la informacion del ejercicio encontrado
     * @throws SpafeuException si el ejercicio no existe o no pertenece al nivel indicado
     */
    @Override
    @Transactional
    public ExerciseDTO findById(Long id, NivelActividadFisicaEnum nivelActividadFisica, Long userId) throws SpafeuException {
        Exercise exercise = exerciseRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new SpafeuException(MensajesKeyConstantesUtil.ERROR_EXERCISE_NO_ENCONTRADO));

        if (exercise.getProtocol() == null || !nivelActividadFisica.equals(exercise.getProtocol().getNivelActividadFisica())) {
            throw new SpafeuException(MensajesKeyConstantesUtil.ERROR_EXERCISE_NIVEL_NO_PERMITIDO);
        }
        
        if(userExerciseSessionRepository.existsByUserIdAndExerciseIdAndCompletadoTrue(userId, id)) {
        	throw new SpafeuException(MensajesKeyConstantesUtil.ERROR_EXERCISE_COMPLETO);
        }
        
        if(!userExerciseSessionRepository.existsByUserIdAndExerciseIdAndCompletadoFalse(userId, id)) {
	        Usuario usuario = usuarioRepository.findById(userId)
	                .orElseThrow(() -> new SpafeuException(MensajesKeyConstantesUtil.ERROR_USUARIO_NO_EXISTE));
	
	        UserExerciseSession session = new UserExerciseSession();
	        session.setUser(usuario);
	        session.setExercise(exercise);
	        session.setFechaInicio(LocalDateTime.now());
	        session.setMinutosRealizados(0);
	        session.setCompletado(false);
	        userExerciseSessionRepository.save(session);
        }

        return modelMapper.map(exercise, ExerciseDTO.class);
    }

    /**
     * Obtiene los identificadores de ejercicios completados por un usuario.
     *
     * @param userId el identificador del usuario
     * @return {@link Set} con los identificadores de ejercicios completados
     * @throws SpafeuException si ocurre un error de negocio
     */
    @Override
    @Transactional(readOnly = true)
    public Set<Long> findCompletedExerciseIdsByUserId(Long userId) throws SpafeuException {
        return userExerciseSessionRepository.findCompletedExerciseIdsByUserId(userId, true);
    }

    /**
     * Completa la sesión de ejercicio activa de un usuario calculando
     * los minutos realizados entre la fecha de inicio y la fecha de fin,
     * evaluando si el usuario completa el protocolo y asciende de nivel.
     *
     * @param exerciseId el identificador del ejercicio
     * @param userId el identificador del usuario
     * @return {@link ExerciseCompletionResultDTO} con el resultado de la operación
     * @throws SpafeuException si no existe una sesión activa para completar
     */
    @Override
    @Transactional
    public ExerciseCompletionResultDTO completeExerciseSession(Long exerciseId, Long userId) throws SpafeuException {
        UserExerciseSession session = userExerciseSessionRepository.findByUserIdAndExerciseIdAndCompletadoFalse(userId, exerciseId)
                .orElseThrow(() -> new SpafeuException(MensajesKeyConstantesUtil.ERROR_EXERCISE_SESSION_NO_ENCONTRADA));

        session.setFechaFin(LocalDateTime.now());
        long minutos = ChronoUnit.MINUTES.between(session.getFechaInicio(), session.getFechaFin());
        session.setMinutosRealizados((int) minutos);
        session.setCompletado(true);
        userExerciseSessionRepository.save(session);

        UserHealthProfile profile = userHealthProfileRepository.findById(userId)
                .orElseThrow(() -> new SpafeuException(MensajesKeyConstantesUtil.ERROR_USER_HEALTH_PROFILE_NO_ENCONTRADO));

        NivelActividadFisicaEnum nivelActual = profile.getNivelActividadFisica();

        List<Exercise> protocolExercises = exerciseRepository.findByProtocolNivelActividadFisica(nivelActual);
        Set<Long> protocolExerciseIds = protocolExercises.stream()
                .map(Exercise::getId)
                .collect(Collectors.toSet());

        Set<Long> completedExerciseIds = new HashSet<>(userExerciseSessionRepository.findCompletedExerciseIdsByUserId(userId, true));
        completedExerciseIds.add(exerciseId);

        ExerciseCompletionResultDTO result = new ExerciseCompletionResultDTO();
        result.setNivelAnterior(nivelActual);

        if (!completedExerciseIds.containsAll(protocolExerciseIds)) {
            result.setSubioNivel(Boolean.FALSE);
            result.setUltimoNivel(Boolean.FALSE);
            result.setNivelNuevo(null);
            result.setMensaje(MensajesKeyConstantesUtil.SUCCESS_EXERCISE_COMPLETADO);
            return result;
        }

        if (NivelActividadFisicaEnum.NIVEL_4.equals(nivelActual)) {
            result.setSubioNivel(Boolean.FALSE);
            result.setUltimoNivel(Boolean.TRUE);
            result.setNivelNuevo(NivelActividadFisicaEnum.NIVEL_4);
            result.setMensaje(MensajesKeyConstantesUtil.SUCCESS_ULTIMO_NIVEL_COMPLETADO);
            return result;
        }

        NivelActividadFisicaEnum siguienteNivel = NivelActividadFisicaEnum.values()[nivelActual.ordinal() + 1];
        profile.setNivelActividadFisica(siguienteNivel);
        userHealthProfileRepository.save(profile);

        userLevelHistoryService.registrarCambioNivel(userId, nivelActual, siguienteNivel,
                "Completó el protocolo del " + nivelActual.getLabel());

        result.setSubioNivel(Boolean.TRUE);
        result.setUltimoNivel(Boolean.FALSE);  // CORRECCIÓN: acaba de subir, no terminó el último nivel
        result.setNivelNuevo(siguienteNivel);
        result.setMensaje(MensajesKeyConstantesUtil.SUCCESS_NIVEL_SUBIDO);
        return result;
    }
}
