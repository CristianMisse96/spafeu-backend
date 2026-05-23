package edu.co.udes.spafeu.web.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.co.udes.spafeu.web.app.models.entities.Question;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Repositorio de la entidad Question para acceso a datos de las preguntas del cuestionario.</p>
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
