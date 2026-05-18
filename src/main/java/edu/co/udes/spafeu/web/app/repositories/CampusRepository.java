package edu.co.udes.spafeu.web.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.co.udes.spafeu.web.app.models.entities.Campus;

@Repository
public interface CampusRepository extends CrudRepository<Campus, Long> {

    @EntityGraph(attributePaths = {"campusPrograms", "campusPrograms.program"})
    @Query("SELECT c FROM Campus c ORDER BY c.id ASC")
    List<Campus> findAllWithAcademicPrograms();
}
