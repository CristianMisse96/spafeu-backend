package edu.co.udes.spafeu.web.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.co.udes.spafeu.web.app.models.entities.UserAnswer;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Repositorio de la entidad UserAnswer para acceso a datos de las respuestas del usuario.</p>
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    List<UserAnswer> findByUserId(Long userId);
}
