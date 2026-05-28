package edu.co.udes.spafeu.web.app.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.co.udes.spafeu.web.app.models.entities.UserExerciseSession;

public interface UserExerciseSessionRepository extends JpaRepository<UserExerciseSession, Long> {
	
	@Query("SELECT s.exercise.id FROM UserExerciseSession s WHERE s.user.id = :userId AND s.completado = :complete")
	Set<Long> findCompletedExerciseIdsByUserId(@Param("userId")  Long userId, @Param("complete")  boolean complete);
	
	boolean existsByUserIdAndExerciseIdAndCompletadoTrue(Long userId, Long exerciseId);
}
