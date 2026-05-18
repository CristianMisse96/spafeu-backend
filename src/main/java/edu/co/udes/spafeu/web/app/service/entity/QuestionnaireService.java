package edu.co.udes.spafeu.web.app.service.entity;

import java.util.Set;
import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.questionnaire.QuestionnaireDTO;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Interfaz Service con lógica de negocio para Questionnaire.
 * Define el contrato de operaciones de consulta sobre cuestionarios de salud.
 * </p>
 *
 * @author [Cristian Misse -cgmisse@gmail.com]
 */
public interface QuestionnaireService {

    /**
     * Obtiene la lista de todos los cuestionarios registrados en el sistema,
     * con sus preguntas y opciones de respuesta cargadas completamente.
     *
     * @return Lista de {@link QuestionnaireDTO} con toda la información de los cuestionarios
     * @throws SpafeuException si ocurre un error al consultar los cuestionarios en la base de datos
     */
    Set<QuestionnaireDTO> findAllQuestionnaires() throws SpafeuException;
}
