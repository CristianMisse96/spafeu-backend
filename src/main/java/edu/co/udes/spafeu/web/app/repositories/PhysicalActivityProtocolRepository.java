package edu.co.udes.spafeu.web.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.co.udes.spafeu.web.app.models.entities.PhysicalActivityProtocol;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Repositorio de la entidad PhysicalActivityProtocol.</p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
@Repository
public interface PhysicalActivityProtocolRepository extends CrudRepository<PhysicalActivityProtocol, Long> {

    /**
     * Obtiene todos los protocolos de actividad física sin incluir los ejercicios,
     * realizando fetch eager de la duración sugerida y los objetivos.
     *
     * @return Lista de {@link PhysicalActivityProtocol} ordenada por ID ascendente
     */
    @Query("SELECT DISTINCT p FROM PhysicalActivityProtocol p "
         + "JOIN FETCH p.suggestedDuration "
         + "JOIN FETCH p.objectives "
         + "ORDER BY p.id ASC")
    List<PhysicalActivityProtocol> findAllWithoutExercises();

    /**
     * Obtiene los protocolos de actividad física filtrados por nivel de actividad física,
     * sin incluir los ejercicios, realizando fetch eager de la duración sugerida y los objetivos.
     *
     * @param nivelActividadFisica el nivel de actividad física por el cual filtrar
     * @return Lista de {@link PhysicalActivityProtocol} filtrada y ordenada por ID ascendente
     */
    @Query("SELECT p FROM PhysicalActivityProtocol p "
         + "JOIN FETCH p.suggestedDuration "
         + "JOIN FETCH p.objectives "
         + "WHERE p.nivelActividadFisica = :nivelActividadFisica "
         + "ORDER BY p.id ASC")
    PhysicalActivityProtocol findAllByNivelActividadFisicaWithoutExercises(
            @Param("nivelActividadFisica") NivelActividadFisicaEnum nivelActividadFisica);
    
}
