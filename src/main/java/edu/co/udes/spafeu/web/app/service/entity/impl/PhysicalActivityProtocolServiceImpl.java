package edu.co.udes.spafeu.web.app.service.entity.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udes.spafeu.web.app.exception.SpafeuException;
import edu.co.udes.spafeu.web.app.models.dto.PhysicalActivityProtocolDTO;
import edu.co.udes.spafeu.web.app.models.entities.PhysicalActivityProtocol;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.repositories.PhysicalActivityProtocolRepository;
import edu.co.udes.spafeu.web.app.service.entity.PhysicalActivityProtocolService;
import lombok.RequiredArgsConstructor;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Implementación de la interfaz {@link PhysicalActivityProtocolService}
 * con la lógica de negocio para la gestión de protocolos de actividad física.</p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
@Service
@RequiredArgsConstructor
public class PhysicalActivityProtocolServiceImpl implements PhysicalActivityProtocolService {

    private final PhysicalActivityProtocolRepository physicalActivityProtocolRepository;
    private final ModelMapper modelMapper;

    /**
     * Obtiene todos los protocolos de actividad física sin incluir los ejercicios.
     * Mapea las entidades a DTOs y transforma el nivel de actividad física a su etiqueta.
     *
     * @return Lista de {@link PhysicalActivityProtocolDTO}
     * @throws SpafeuException si ocurre un error de negocio
     */
    @Override
    @Transactional(readOnly = true)
    public List<PhysicalActivityProtocolDTO> findAllWithoutExercises() throws SpafeuException {
        List<PhysicalActivityProtocol> protocols = physicalActivityProtocolRepository.findAllWithoutExercises();

        return protocols.stream()
                .map(entity -> modelMapper.map(entity, PhysicalActivityProtocolDTO.class))
                .toList();
    }

    /**
     * Obtiene los protocolos de actividad fisica filtrados por nivel de actividad fisica
     * sin incluir los ejercicios.
     *
     * @param nivelActividadFisica el nivel de actividad fisica por el cual filtrar
     * @return Lista de {@link PhysicalActivityProtocolDTO}
     * @throws SpafeuException si ocurre un error de negocio
     */
    @Override
    @Transactional(readOnly = true)
    public PhysicalActivityProtocolDTO findAllByNivelActividadFisicaWithoutExercises(NivelActividadFisicaEnum nivelActividadFisica) throws SpafeuException {
        PhysicalActivityProtocol protocol = physicalActivityProtocolRepository.findAllByNivelActividadFisicaWithoutExercises(nivelActividadFisica);

        return modelMapper.map(protocol, PhysicalActivityProtocolDTO.class);
    }

  
}
