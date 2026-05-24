package edu.co.udes.spafeu.web.app.service.entity.impl;

import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.ERROR_CAMPUS_PROGRAM_NO_ENCONTRADO;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.ERROR_PERFIL_SALUD_YA_EXISTE;
import static edu.co.udes.spafeu.web.app.util.MensajesKeyConstantesUtil.KEY_ERROR_PETICION_GENERICO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.UserAnswerDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserHealthProfileDTO;
import edu.co.udes.spafeu.web.app.models.dto.UsuarioDTO;
import edu.co.udes.spafeu.web.app.models.entities.CampusProgram;
import edu.co.udes.spafeu.web.app.models.entities.Option;
import edu.co.udes.spafeu.web.app.models.entities.Question;
import edu.co.udes.spafeu.web.app.models.entities.UserAnswer;
import edu.co.udes.spafeu.web.app.models.entities.UserHealthProfile;
import edu.co.udes.spafeu.web.app.models.entities.Usuario;
import edu.co.udes.spafeu.web.app.models.enums.EstadoKycEnum;
import edu.co.udes.spafeu.web.app.repositories.CampusProgramRepository;
import edu.co.udes.spafeu.web.app.repositories.OptionRepository;
import edu.co.udes.spafeu.web.app.repositories.QuestionRepository;
import edu.co.udes.spafeu.web.app.repositories.UserAnswerRepository;
import edu.co.udes.spafeu.web.app.repositories.UserHealthProfileRepository;
import edu.co.udes.spafeu.web.app.repositories.UsuarioRepository;
import edu.co.udes.spafeu.web.app.service.entity.UserHealthProfileService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserHealthProfileServiceImpl implements UserHealthProfileService {

    private final UsuarioRepository usuarioRepository;
    private final UserHealthProfileRepository userHealthProfileRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final CampusProgramRepository campusProgramRepository;

    @Override
    @Transactional
    public UsuarioDTO registerHealthProfile(UserHealthProfileDTO dto, Long userId) throws SpafeuException {
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new SpafeuException(KEY_ERROR_PETICION_GENERICO));

        if (userHealthProfileRepository.existsByUserId(userId)) {
            throw new SpafeuException(ERROR_PERFIL_SALUD_YA_EXISTE);
        }

        CampusProgram campusProgram = campusProgramRepository
                .findByCampusIdAndProgramId(dto.getCampusId(), dto.getProgramId())
                .orElseThrow(() -> new SpafeuException(ERROR_CAMPUS_PROGRAM_NO_ENCONTRADO));

        UserHealthProfile profileEntity = toProfileEntity(dto);
        profileEntity.setUser(user);
        profileEntity.setCampusProgram(campusProgram);

        UserHealthProfile savedProfile = userHealthProfileRepository.save(profileEntity);

        if (dto.getAnswers() != null && !dto.getAnswers().isEmpty()) {
            List<UserAnswer> answersToSave = new ArrayList<>();
            for (UserAnswerDTO answerDTO : dto.getAnswers()) {
                Question question = questionRepository.findById(answerDTO.getQuestionId())
                        .orElseThrow(() -> new SpafeuException(KEY_ERROR_PETICION_GENERICO));
                Option option = optionRepository.findById(answerDTO.getOptionId())
                        .orElseThrow(() -> new SpafeuException(KEY_ERROR_PETICION_GENERICO));

                UserAnswer answerEntity = new UserAnswer();
                answerEntity.setUser(user);
                answerEntity.setQuestion(question);
                answerEntity.setOption(option);
                answersToSave.add(answerEntity);
            }
            userAnswerRepository.saveAll(answersToSave);
        }

        UsuarioDTO response = new UsuarioDTO();
        response.setId(user.getId());
        response.setEstadoKyc(savedProfile.getEstadoKyc());

        return response;
    }

	private UserHealthProfile toProfileEntity(UserHealthProfileDTO dto) {
		
		UserHealthProfile profileEntity = new UserHealthProfile();
		
		profileEntity.setFullName(dto.getFullName());
		profileEntity.setSemester(dto.getSemester());
		profileEntity.setBirthDate(dto.getBirthDate());
		profileEntity.setSex(dto.getSex());
		profileEntity.setWeightKg(dto.getWeightKg());
		profileEntity.setHeightM(dto.getHeightM());
		profileEntity.setEstadoKyc(EstadoKycEnum.COMPLETO);
		
		return profileEntity;
	}
}
