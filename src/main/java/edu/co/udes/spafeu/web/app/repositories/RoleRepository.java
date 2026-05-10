package edu.co.udes.spafeu.web.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.co.udes.spafeu.web.app.models.entities.Role;
import edu.co.udes.spafeu.web.app.models.enums.RolEnum;

/**
 * <p>
 * Titulo: Proyecto SPAFEU
 * </p>
 * <p>
 * Descripción: Repositorio de la entidad Role
 * </p>
 *
 * @author Cristian Misse
 **/

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	
	Optional<Role> findByRol(RolEnum rol);

}
