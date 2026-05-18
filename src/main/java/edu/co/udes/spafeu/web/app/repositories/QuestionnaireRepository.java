package edu.co.udes.spafeu.web.app.repositories;

import java.util.Set;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import edu.co.udes.spafeu.web.app.models.entities.Questionnaire;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripcion: Repositorio de la entidad Questionnaire.</p>
 * <p>Estrategia anti-N+1: Utiliza @EntityGraph con 3 niveles de profundidad para cargar
 * en una sola consulta: Questionnaire -> questions -> options -> option,
 * evitando consultas adicionales por cada nivel de la jerarquia.</p>
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Repository
public interface QuestionnaireRepository extends CrudRepository<Questionnaire, Long> {

    /**
     * Recupera todos los cuestionarios con sus preguntas, opciones de pregunta y opciones
     * cargadas en una sola consulta mediante @EntityGraph.
     *
     * @return lista de Questionnaire con relaciones completas cargadas
     */
    @EntityGraph(attributePaths = {"questions", "questions.options", "questions.options.option"})
    @Query("SELECT q FROM Questionnaire q")
    Set<Questionnaire> findAllWithDetails();
}
