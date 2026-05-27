package edu.co.udes.spafeu.web.app.service.entity;

import java.util.List;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.PhysicalActivityProtocolDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripcion: Interfaz Service con logica de negocio para PhysicalActivityProtocol.</p>
 * @author Cristian Misse - cgmisse@gmail.com
 */
public interface PhysicalActivityProtocolService {

    /**
     * Obtiene todos los protocolos de actividad fisica sin incluir los ejercicios.
     *
     * @return Lista de {@link PhysicalActivityProtocolDTO}
     * @throws SpafeuException si ocurre un error de negocio
     */
    List<PhysicalActivityProtocolDTO> findAllWithoutExercises() throws SpafeuException;

    /**
     * Obtiene todos los protocolos de actividad fisica filtrados por nivel de actividad fisica
     * sin incluir los ejercicios.
     *
     * @param nivelActividadFisica el nivel de actividad fisica por el cual filtrar
     * @return Lista de {@link PhysicalActivityProtocolDTO}
     * @throws SpafeuException si ocurre un error de negocio
     */
    PhysicalActivityProtocolDTO findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum nivelActividadFisica) throws SpafeuException;
}
