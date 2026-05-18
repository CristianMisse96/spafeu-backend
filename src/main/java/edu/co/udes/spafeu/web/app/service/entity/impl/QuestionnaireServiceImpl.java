package edu.co.udes.spafeu.web.app.service.entity.impl;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.questionnaire.OptionDTO;
import edu.co.udes.spafeu.web.app.models.dto.questionnaire.QuestionDTO;
import edu.co.udes.spafeu.web.app.models.dto.questionnaire.QuestionnaireDTO;
import edu.co.udes.spafeu.web.app.models.entities.Option;
import edu.co.udes.spafeu.web.app.models.entities.Question;
import edu.co.udes.spafeu.web.app.models.entities.QuestionOption;
import edu.co.udes.spafeu.web.app.models.entities.Questionnaire;
import edu.co.udes.spafeu.web.app.repositories.QuestionnaireRepository;
import edu.co.udes.spafeu.web.app.service.entity.QuestionnaireService;
import lombok.RequiredArgsConstructor;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Implementación de la lógica de negocio para Questionnaire.</p>
 * @author [Cristian Misse -cgmisse@gmail.com]
 */
@Service
@RequiredArgsConstructor
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;

    @Override
    @Transactional(readOnly = true)
    public Set<QuestionnaireDTO> findAllQuestionnaires() throws SpafeuException {
       
        Set<Questionnaire> questionnaires = questionnaireRepository.findAllWithDetails();
        
        return questionnaires.stream()
                .map(this::toQuestionnaireDTO)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
    
    private QuestionnaireDTO toQuestionnaireDTO(Questionnaire q) {
        QuestionnaireDTO dto = new QuestionnaireDTO();
        dto.setId(q.getId());
        dto.setName(q.getName());

        Set<QuestionDTO> questions = q.getQuestions().stream()
                .map(this::toQuestionDTO)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        dto.setQuestions(questions);

        return dto;
    }
    
    private QuestionDTO toQuestionDTO(Question q) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(q.getId());
        dto.setText(q.getText());

        Set<OptionDTO> options = q.getOptions().stream()
                .map(this::toOptionDTO)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        dto.setOptions(options);

        return dto;
    }
    
    private OptionDTO toOptionDTO(QuestionOption qo) {
        Option option = qo.getOption();

        OptionDTO dto = new OptionDTO();
        dto.setId(option.getId());
        dto.setLabel(option.getLabel());

        return dto;
    }
}
