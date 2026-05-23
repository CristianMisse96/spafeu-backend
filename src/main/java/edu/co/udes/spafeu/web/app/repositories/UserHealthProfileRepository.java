package edu.co.udes.spafeu.web.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.co.udes.spafeu.web.app.models.entities.UserHealthProfile;

/**
 * <p>Titulo: Proyecto SPAFEU</p>
 * <p>Descripción: Repositorio de la entidad UserHealthProfile para acceso a datos del perfil de salud del usuario.</p>
 * @author AGENT-DATA-EXPERT-SPAFEU
 */
@Repository
public interface UserHealthProfileRepository extends JpaRepository<UserHealthProfile, Long> {

    boolean existsByUserId(Long userId);
}
