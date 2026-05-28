package edu.co.udes.spafeu.web.app.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.co.udes.spafeu.web.app.models.entities.UserExerciseSession;

public interface UserExerciseSessionRepository extends JpaRepository<UserExerciseSession, Long> {
	
	@Query("SELECT s.exercise.id FROM UserExerciseSession s WHERE s.user.id = :userId AND s.completado = :complete")
	Set<Long> findCompletedExerciseIdsByUserId(@Param("userId")  Long userId, @Param("complete")  boolean complete);
	
	boolean existsByUserIdAndExerciseIdAndCompletadoTrue(Long userId, Long exerciseId);

	boolean existsByUserIdAndExerciseIdAndCompletadoFalse(Long userId, Long id);

	Optional<UserExerciseSession> findByUserIdAndExerciseIdAndCompletadoFalse(Long userId, Long exerciseId);

	@Query("SELECT COUNT(s) FROM UserExerciseSession s WHERE s.user.id = :userId AND s.completado = true")
	Long countCompletedSessionsByUserId(@Param("userId") Long userId);

	@Query("SELECT COALESCE(SUM(s.minutosRealizados), 0) FROM UserExerciseSession s WHERE s.user.id = :userId AND s.completado = true")
	Integer sumMinutesCompletedByUserId(@Param("userId") Long userId);

	@Query("SELECT MAX(s.fechaInicio) FROM UserExerciseSession s WHERE s.user.id = :userId AND s.completado = true")
	Optional<LocalDateTime> findLastActivityDateByUserId(@Param("userId") Long userId);

	@Query("SELECT s FROM UserExerciseSession s WHERE s.user.id = :userId AND s.completado = true ORDER BY s.fechaInicio DESC")
	List<UserExerciseSession> findRecentCompletedSessionsByUserId(@Param("userId") Long userId);
}
