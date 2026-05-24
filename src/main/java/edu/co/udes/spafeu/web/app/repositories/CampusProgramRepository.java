package edu.co.udes.spafeu.web.app.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.co.udes.spafeu.web.app.models.entities.CampusProgram;
import edu.co.udes.spafeu.web.app.models.entities.CampusProgramId;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Repositorio de la entidad CampusProgram para gestionar la relación entre campus y programas académicos.</p>
 * @author SPAFEU Dev Team
 */
@Repository
public interface CampusProgramRepository extends JpaRepository<CampusProgram, CampusProgramId> {

    /**
     * Busca un CampusProgram por el ID compuesto (campusId + programId).
     *
     * @param campusId  identificador del campus
     * @param programId identificador del programa académico
     * @return Optional con el CampusProgram encontrado, o vacío si no existe
     */
    Optional<CampusProgram> findByCampusIdAndProgramId(Long campusId, Long programId);
}
