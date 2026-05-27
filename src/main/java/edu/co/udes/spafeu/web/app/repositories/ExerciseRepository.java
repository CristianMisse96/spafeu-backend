package edu.co.udes.spafeu.web.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.co.udes.spafeu.web.app.models.entities.Exercise;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Repositorio de la entidad Exercise.</p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {

    /**
     * Obtiene los ejercicios filtrados por nivel de actividad física del protocolo,
     * realizando fetch eager del tipo de actividad, tipo de intensidad y duración.
     *
     * @param nivelActividadFisica el nivel de actividad física por el cual filtrar
     * @return Lista de {@link Exercise} filtrada y ordenada por ID ascendente
     */
    @Query("SELECT e FROM Exercise e "
         + "JOIN FETCH e.tipoActividadFisica "
         + "JOIN FETCH e.tipoIntensidad "
         + "JOIN FETCH e.duracion "
         + "JOIN e.protocol p "
         + "WHERE p.nivelActividadFisica = :nivelActividadFisica "
         + "ORDER BY e.id ASC")
    List<Exercise> findByProtocolNivelActividadFisica(
            @Param("nivelActividadFisica") NivelActividadFisicaEnum nivelActividadFisica);

    /**
     * Obtiene un ejercicio por su ID realizando JOIN FETCH en las relaciones
     * ManyToOne para evitar el problema N+1.
     *
     * @param id el identificador del ejercicio
     * @return {@link Optional} que contiene el ejercicio si se encuentra
     */
    @Query("SELECT e FROM Exercise e "
         + "JOIN FETCH e.tipoActividadFisica "
         + "JOIN FETCH e.tipoIntensidad "
         + "JOIN FETCH e.duracion "
         + "JOIN FETCH e.protocol "
         + "WHERE e.id = :id")
    Optional<Exercise> findByIdWithDetails(@Param("id") Long id);

}
