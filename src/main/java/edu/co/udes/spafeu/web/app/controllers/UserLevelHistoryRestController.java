package edu.co.udes.spafeu.web.app.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserLevelHistoryDTO;
import edu.co.udes.spafeu.web.app.service.task.UserLevelHistoryServiceTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Controlador REST para la consulta del histórico de niveles de actividad física del usuario.
 * </p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
@Tag(name = "Histórico de Niveles", description = "Endpoints para consultar el histórico de niveles de actividad física del usuario")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/user-level-history")
@RequiredArgsConstructor
public class UserLevelHistoryRestController {

    private final UserLevelHistoryServiceTask userLevelHistoryServiceTask;

    /**
     * Obtiene el histórico de niveles de actividad física del usuario autenticado,
     * ordenado descendentemente por fecha.
     *
     * @return ResponseEntity con RespuestaServicioDTO conteniendo la lista de registros históricos de nivel
     */
    @Operation(
            summary = "Obtener histórico de niveles del usuario autenticado",
            description = "Retorna el historial de cambios de nivel de actividad física del usuario autenticado, ordenado descendentemente por fecha. Requiere autenticación JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de niveles obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "Token JWT no valido o expirado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<RespuestaServicioDTO<List<UserLevelHistoryDTO>>> findByUserId() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return userLevelHistoryServiceTask.findByUserId(userId);
    }
}
