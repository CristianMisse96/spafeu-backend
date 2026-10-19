package edu.co.udes.spafeu.web.app.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.co.udes.spafeu.web.app.models.entities.UserLevelHistory;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Repositorio de la entidad UserLevelHistory para acceso al histórico de niveles del usuario.
 * </p>
 *
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Repository
public interface UserLevelHistoryRepository extends JpaRepository<UserLevelHistory, Long> {

    /**
     * Busca el histórico de niveles de un usuario ordenado descendientemente por fecha de cambio.
     *
     * @param userId identificador del usuario
     * @return lista de registros históricos de nivel
     */
    List<UserLevelHistory> findByUserIdOrderByFechaCambioDesc(Long userId);

    /**
     * Indica si existe al menos un registro histórico asociado al usuario.
     *
     * @param userId identificador del usuario
     * @return true si existe al menos un registro, false en caso contrario
     */
    boolean existsByUserId(Long userId);
}
