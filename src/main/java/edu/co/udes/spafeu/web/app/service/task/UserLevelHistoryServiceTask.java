package edu.co.udes.spafeu.web.app.service.task;

import java.util.List;
import org.springframework.http.ResponseEntity;
import edu.co.udes.spafeu.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udes.spafeu.web.app.models.dto.UserLevelHistoryDTO;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Interfaz ServiceTask para operaciones REST de UserLevelHistory.
 * </p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 */
public interface UserLevelHistoryServiceTask {

    /**
     * Obtiene el historico de niveles de un usuario ordenado descendientemente por fecha
     * y construye la respuesta de servicio con mensaje de exito.
     *
     * @param userId el identificador del usuario
     * @return ResponseEntity con {@link RespuestaServicioDTO} conteniendo el historico de niveles
     */
    ResponseEntity<RespuestaServicioDTO<List<UserLevelHistoryDTO>>> findByUserId(Long userId);
}
