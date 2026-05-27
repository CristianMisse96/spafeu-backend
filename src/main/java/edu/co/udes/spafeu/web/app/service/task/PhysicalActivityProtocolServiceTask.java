package edu.co.udes.spafeu.web.app.service.task;

import java.util.List;

import org.springframework.http.ResponseEntity;

import edu.co.udes.spafeu.web.app.models.dto.ExerciseGroupedByActivityTypeDTO;
import edu.co.udes.spafeu.web.app.models.dto.PhysicalActivityProtocolDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripcion: Interfaz ServiceTask para operaciones REST de PhysicalActivityProtocol.</p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
public interface PhysicalActivityProtocolServiceTask {

    /**
     * Obtiene todos los protocolos de actividad fisica sin incluir los ejercicios.
     *
     * @return ResponseEntity con la lista de PhysicalActivityProtocolDTO
     */
    ResponseEntity<RespuestaServicioDTO<List<PhysicalActivityProtocolDTO>>> findAllWithoutExercises();

    /**
     * Obtiene los protocolos de actividad fisica filtrados por nivel de actividad fisica
     * sin incluir los ejercicios.
     *
     * @param nivelActividadFisica el nivel de actividad fisica por el cual filtrar
     * @return ResponseEntity con la lista de PhysicalActivityProtocolDTO filtrada
     */
    ResponseEntity<RespuestaServicioDTO<PhysicalActivityProtocolDTO>> findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum nivelActividadFisica);

    /**
     * Obtiene los ejercicios agrupados por tipo de actividad filtrados por nivel de actividad fisica.
     *
     * @param nivelActividadFisica el nivel de actividad fisica por el cual filtrar
     * @return ResponseEntity con la lista de ExerciseGroupedByActivityTypeDTO agrupada
     */
    ResponseEntity<RespuestaServicioDTO<List<ExerciseGroupedByActivityTypeDTO>>> findExercisesByNivelActividadFisicaGroupedByActivityType(NivelActividadFisicaEnum nivelActividadFisica);
}
