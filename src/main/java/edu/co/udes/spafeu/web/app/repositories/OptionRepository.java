package edu.co.udes.spafeu.web.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.co.udes.spafeu.web.app.models.entities.Option;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Repositorio de la entidad Option para acceso a datos de las opciones de respuesta.</p>
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}
