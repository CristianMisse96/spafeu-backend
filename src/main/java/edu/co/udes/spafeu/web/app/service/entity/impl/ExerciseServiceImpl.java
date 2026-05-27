package edu.co.udes.spafeu.web.app.service.entity.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.ExerciseDTO;
import edu.co.udes.spafeu.web.app.models.entities.Exercise;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.repositories.ExerciseRepository;
import edu.co.udes.spafeu.web.app.service.entity.ExerciseService;
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
    @Transactional(readOnly = true)
    public ExerciseDTO findById(Long id, NivelActividadFisicaEnum nivelActividadFisica) throws SpafeuException {
        Exercise exercise = exerciseRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new SpafeuException(MensajesKeyConstantesUtil.ERROR_EXERCISE_NO_ENCONTRADO));

        if (exercise.getProtocol() == null || !nivelActividadFisica.equals(exercise.getProtocol().getNivelActividadFisica())) {
            throw new SpafeuException(MensajesKeyConstantesUtil.ERROR_EXERCISE_NIVEL_NO_PERMITIDO);
        }

        return modelMapper.map(exercise, ExerciseDTO.class);
    }
}
