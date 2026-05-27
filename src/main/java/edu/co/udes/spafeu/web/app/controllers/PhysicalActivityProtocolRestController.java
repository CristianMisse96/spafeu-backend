package edu.co.udes.spafeu.web.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.co.udes.spafeu.web.app.models.dto.PhysicalActivityProtocolDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.enums.NivelActividadFisicaEnum;
import edu.co.udes.spafeu.web.app.service.task.PhysicalActivityProtocolServiceTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Protocolos de Actividad Física", description = "Endpoints para la gestión de protocolos de actividad física")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/physical-activity-protocols")
@RequiredArgsConstructor
public class PhysicalActivityProtocolRestController {

    private final PhysicalActivityProtocolServiceTask physicalActivityProtocolServiceTask;

    @Operation(summary = "Listar protocolos de actividad física filtrados por nivel sin ejercicios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de protocolos obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetro de nivel de actividad física no válido o faltante"),
            @ApiResponse(responseCode = "401", description = "Token JWT no válido o expirado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<RespuestaServicioDTO<PhysicalActivityProtocolDTO>> findAllWithoutExercises(
            @Parameter(description = "Nivel de actividad física para filtrar los protocolos", required = true)
            @RequestParam NivelActividadFisicaEnum nivelActividadFisica) {
        return physicalActivityProtocolServiceTask.findAllByNivelActividadFisicaWithoutExercises(nivelActividadFisica);
    }
}
