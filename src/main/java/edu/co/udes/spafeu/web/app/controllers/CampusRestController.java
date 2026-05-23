package edu.co.udes.spafeu.web.app.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.co.udes.spafeu.web.app.models.dto.CampusDTO;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.service.task.CampusServiceTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Gestión de Campus", description = "Endpoints para la gestión de campus y sus programas académicos")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/campuses")
@RequiredArgsConstructor
public class CampusRestController {

    private final CampusServiceTask campusServiceTask;

    @Operation(summary = "Listar todos los campus con sus programas académicos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de campus obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT no válido o expirado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<RespuestaServicioDTO<List<CampusDTO>>> findAll() {
        return campusServiceTask.findAllCampusesWithPrograms();
    }
}
