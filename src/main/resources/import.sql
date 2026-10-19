-- =========================
-- OPTIONS (Sí / No)
-- =========================
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (1, 'Sí');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (2, 'No');

-- =========================
-- QUESTIONNAIRE 1: PAR-Q
-- =========================
INSERT INTO QUESTIONNAIRE (ID_QUESTIONNAIRE, NAME) VALUES (1, 'PAR-Q');

-- Questions PAR-Q
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (1, 1, '¿Alguna vez un médico le ha indicado que tiene un problema cardíaco y que solo debe realizar actividad física bajo supervisión médica?');
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (2, 1, '¿Siente dolor en el pecho cuando realiza actividad física?');
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (3, 1, 'En el último mes, ¿ha tenido dolor en el pecho incluso sin realizar actividad física?');
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (4, 1, '¿Ha perdido el equilibrio debido a mareos o ha perdido el conocimiento en algún momento?');
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (5, 1, '¿Tiene algún problema óseo o articular que pueda empeorar al realizar actividad física?');
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (6, 1, '¿Actualmente algún médico le ha prescrito medicamentos para controlar la presión arterial o alguna condición cardíaca?');
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (7, 1, '¿Conoce alguna otra razón de salud por la cual no debería realizar actividad física?');
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (8, 1, '¿Tiene actualmente alguna lesión musculoesquelética?');
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (9, 1, '¿Se encuentra embarazada?');

-- QuestionOption PAR-Q (todas Sí/No)
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (1,1,1);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (2,1,2);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (3,2,1);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (4,2,2);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (5,3,1);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (6,3,2);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (7,4,1);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (8,4,2);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (9,5,1);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (10,5,2);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (11,6,1);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (12,6,2);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (13,7,1);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (14,7,2);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (15,8,1);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (16,8,2);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (17,9,1);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (18,9,2);

-- =========================
-- QUESTIONNAIRE 2: Historial
-- =========================
INSERT INTO QUESTIONNAIRE (ID_QUESTIONNAIRE, NAME) VALUES (2, 'Historial de actividad física');

-- Options reutilizables
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (3, '0 días');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (4, '1-2 días');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (5, '3-4 días');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (6, '5 o más días');

INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (7, 'Menos de 20 minutos');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (8, '20-30 minutos');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (9, '30-60 minutos');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (10, 'Más de 60 minutos');

INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (11, 'Caminata o actividad ligera');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (12, 'Actividad moderada');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (13, 'Actividad vigorosa');

INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (14, 'Menos de 4 horas');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (15, '4-6 horas');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (16, '6-8 horas');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (17, 'Más de 8 horas');

INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (18, 'Muy bajo');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (19, 'Bajo');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (20, 'Moderado');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (21, 'Bueno');
INSERT INTO OPTION (ID_OPTION, LABEL) VALUES (22, 'Muy bueno');

-- Questions Historial
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (10, 2, 'Durante los últimos 7 días, ¿cuántos días realizó actividad física moderada o vigorosa?');
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (11, 2, 'En los días que realiza actividad física, ¿cuánto tiempo dura aproximadamente cada sesión?');
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (12, 2, '¿Qué tipo de actividad realiza con mayor frecuencia?');
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (13, 2, 'En un día típico, ¿cuánto tiempo permanece sentado?');
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONNAIRE, TEXT) VALUES (14, 2, '¿Cómo considera su nivel actual de condición física?');

-- QuestionOption Historial
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (19,10,3);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (20,10,4);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (21,10,5);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (22,10,6);

INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (23,11,7);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (24,11,8);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (25,11,9);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (26,11,10);

INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (27,12,11);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (28,12,12);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (29,12,13);

INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (30,13,14);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (31,13,15);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (32,13,16);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (33,13,17);

INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (34,14,18);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (35,14,19);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (36,14,20);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (37,14,21);
INSERT INTO QUESTION_OPTION (ID_QUESTION_OPTION, ID_QUESTION, ID_OPTION) VALUES (38,14,22);

-- ============================================
-- PASO 1: Insertar CAMPUS con IDs definidos
-- ============================================

INSERT INTO CAMPUS (ID_CAMPUS, NAME) VALUES (1, 'Bogotá');
INSERT INTO CAMPUS (ID_CAMPUS, NAME) VALUES (2, 'Bucaramanga');
INSERT INTO CAMPUS (ID_CAMPUS, NAME) VALUES (3, 'Cúcuta');
INSERT INTO CAMPUS (ID_CAMPUS, NAME) VALUES (4, 'Valledupar');

-- ==========================================================
-- PROGRAMAS ACADÉMICOS con ID ordenado por NAME ASC
-- ==========================================================

INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (1, 'Administración Financiera');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (2, 'Administración de Empresas (Virtual)');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (3, 'Bacteriología y Laboratorio Clínico');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (4, 'Comercio Exterior');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (5, 'Comunicación Social y Periodismo');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (6, 'Contaduría Pública');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (7, 'Contaduría Pública (virtual)');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (8, 'Derecho');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (9, 'Diseño Gráfico');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (10, 'Economía (Virtual)');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (11, 'Enfermería');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (12, 'Fisioterapia');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (13, 'Fonoaudiología');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (14, 'Geología');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (15, 'Ingeniería Agronómica');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (16, 'Ingeniería Agroindustrial');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (17, 'Ingeniería Ambiental');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (18, 'Ingeniería Civil');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (19, 'Ingeniería de Sistemas');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (20, 'Ingeniería de Software');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (21, 'Ingeniería de Software (Virtual)');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (22, 'Ingeniería en Ciencias de la Computación e Inteligencia Artificial');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (23, 'Ingeniería en Energías');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (24, 'Ingeniería Industrial');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (25, 'Ingeniería Industrial (Virtual)');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (26, 'Ingeniería Petroquímica');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (27, 'Instrumentación Quirúrgica');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (28, 'Licenciatura en Educación Infantil');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (29, 'Medicina');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (30, 'Medicina Veterinaria');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (31, 'Medicina Veterinaria y Zootecnia');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (32, 'Mercadeo y Publicidad');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (33, 'Microbiología Industrial');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (34, 'Psicología');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (35, 'Relaciones y Negocios Internacionales (virtual)');
INSERT INTO ACADEMIC_PROGRAM (ID_PROGRAM, NAME) VALUES (36, 'Terapia Ocupacional');

-- Bogotá
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (1, 27, true);

-- Bucaramanga
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 2, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 3, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 5, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 6, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 7, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 8, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 10, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 11, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 12, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 13, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 14, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 16, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 17, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 18, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 20, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 21, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 22, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 23, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 24, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 25, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 26, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 27, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 28, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 29, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 30, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 32, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 33, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 34, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 35, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (2, 36, true);

-- Cúcuta
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 1, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 3, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 4, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 5, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 8, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 9, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 11, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 12, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 19, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 24, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 29, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 32, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 34, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (3, 36, true);

-- Valledupar
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (4, 1, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (4, 3, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (4, 5, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (4, 8, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (4, 9, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (4, 11, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (4, 12, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (4, 15, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (4, 24, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (4, 29, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (4, 31, true);
INSERT INTO CAMPUS_PROGRAM (ID_CAMPUS, ID_PROGRAM, ACTIVE) VALUES (4, 34, true);

-- =========================================
-- DURATION TYPES
-- =========================================
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (1, '30-60 min');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (2, '20-40 min');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (3, '30-45 min');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (4, '3-4 series de 15-20 reps');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (5, '3-4 series de 12-15 reps');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (6, '3-4 series de 15 reps');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (7, '10-15 min');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (8, '1-5 min');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (9, '5-10 min');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (10, '5 min');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (11, '40-90 min');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (12, '15-20 min');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (13, 'Mantener 20-30 segundos por lado');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (14, '5-10 min (6:30 - 7:30 min/km)');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (15, '30 seg por pierna');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (16, '20 repeticiones por pierna');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (17, '20-30 min');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (18, '10-20 min');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (19, '4 series de 8-12 reps');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (20, 'Mantener 30-45 segundos por lado');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (21, 'Mantener 30-40 segundos por lado');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (22, 'Mantener 20-30 segundos');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (23, '(5:30 - 6:00 min/km)');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (24, '30-60 segundos');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (25, '30 repeticiones');
INSERT INTO DURATION_TYPE (ID_DURATION, DESCRIPTION) VALUES (26, '1-2 min');

-- =========================================
-- ACTIVITY TYPES
-- =========================================
INSERT INTO ACTIVITY_TYPE (ID_ACTIVITY_TYPE, NAME) VALUES (1, 'Aeróbica');
INSERT INTO ACTIVITY_TYPE (ID_ACTIVITY_TYPE, NAME) VALUES (2, 'Fuerza y resistencia muscular');
INSERT INTO ACTIVITY_TYPE (ID_ACTIVITY_TYPE, NAME) VALUES (3, 'Coordinación');
INSERT INTO ACTIVITY_TYPE (ID_ACTIVITY_TYPE, NAME) VALUES (4, 'Flexibilidad');
INSERT INTO ACTIVITY_TYPE (ID_ACTIVITY_TYPE, NAME) VALUES (5, 'Vuelta a la calma');

-- =========================================
-- INTENSITY TYPES
-- =========================================
INSERT INTO INTENSITY_TYPE (ID_INTENSITY_TYPE, NAME) VALUES (1, 'Leve 65% Fc MAX');
INSERT INTO INTENSITY_TYPE (ID_INTENSITY_TYPE, NAME) VALUES (2, 'Leve 1RM');
INSERT INTO INTENSITY_TYPE (ID_INTENSITY_TYPE, NAME) VALUES (3, 'Moderada 70% - 75% Fc MAX');
INSERT INTO INTENSITY_TYPE (ID_INTENSITY_TYPE, NAME) VALUES (4, 'Moderada 1RM');
INSERT INTO INTENSITY_TYPE (ID_INTENSITY_TYPE, NAME) VALUES (5, 'Vigorosa 80% - 85% Fc MAX');
INSERT INTO INTENSITY_TYPE (ID_INTENSITY_TYPE, NAME) VALUES (6, 'Vigorosa 1RM');

-- =========================================
-- PHYSICAL ACTIVITY PROTOCOLS
-- =========================================
INSERT INTO PHYSICAL_ACTIVITY_PROTOCOL (ID_PROTOCOL, NIVEL_ACTIVIDAD_FISICA, NAME, DESCRIPTION, ID_DURATION) VALUES (1, 'NIVEL_1', 'Actividad física leve', 'Programa orientado a usuarios sedentarios o con muy baja actividad física. Busca iniciar la adaptación al ejercicio, reducir el sedentarismo y mejorar la movilidad general.', 2);
INSERT INTO PHYSICAL_ACTIVITY_PROTOCOL (ID_PROTOCOL, NIVEL_ACTIVIDAD_FISICA, NAME, DESCRIPTION, ID_DURATION) VALUES (2, 'NIVEL_2', 'Actividad física leve (progresión)', 'Programa orientado a usuarios con actividad física ocasional. Busca mejorar la resistencia aeróbica básica y preparar al usuario para actividades moderadas.', 1);
INSERT INTO PHYSICAL_ACTIVITY_PROTOCOL (ID_PROTOCOL, NIVEL_ACTIVIDAD_FISICA, NAME, DESCRIPTION, ID_DURATION) VALUES (3, 'NIVEL_3', 'Actividad física moderada', 'Programa orientado a usuarios físicamente activos de manera regular. Busca mejorar la condición física general y aumentar la capacidad cardiorrespiratoria.', 1);
INSERT INTO PHYSICAL_ACTIVITY_PROTOCOL (ID_PROTOCOL, NIVEL_ACTIVIDAD_FISICA, NAME, DESCRIPTION, ID_DURATION) VALUES (4, 'NIVEL_4', 'Actividad física vigorosa', 'Programa orientado a usuarios con alto nivel de actividad física. Busca incrementar el rendimiento físico y desarrollar fuerza y resistencia avanzada.', 1);

-- =========================================
-- PROTOCOL OBJECTIVES
-- =========================================

-- Nivel 1
INSERT INTO PROTOCOL_OBJECTIVE (ID_PROTOCOL_OBJECTIVE, ID_PROTOCOL, DESCRIPTION) VALUES (1, 1, 'Iniciar adaptación al ejercicio');
INSERT INTO PROTOCOL_OBJECTIVE (ID_PROTOCOL_OBJECTIVE, ID_PROTOCOL, DESCRIPTION) VALUES (2, 1, 'Reducir el sedentarismo');
INSERT INTO PROTOCOL_OBJECTIVE (ID_PROTOCOL_OBJECTIVE, ID_PROTOCOL, DESCRIPTION) VALUES (3, 1, 'Mejorar la movilidad general');

-- Nivel 2
INSERT INTO PROTOCOL_OBJECTIVE (ID_PROTOCOL_OBJECTIVE, ID_PROTOCOL, DESCRIPTION) VALUES (4, 2, 'Aumentar la frecuencia de actividad física');
INSERT INTO PROTOCOL_OBJECTIVE (ID_PROTOCOL_OBJECTIVE, ID_PROTOCOL, DESCRIPTION) VALUES (5, 2, 'Mejorar la resistencia aeróbica básica');
INSERT INTO PROTOCOL_OBJECTIVE (ID_PROTOCOL_OBJECTIVE, ID_PROTOCOL, DESCRIPTION) VALUES (6, 2, 'Preparar al usuario para actividades moderadas');

-- Nivel 3
INSERT INTO PROTOCOL_OBJECTIVE (ID_PROTOCOL_OBJECTIVE, ID_PROTOCOL, DESCRIPTION) VALUES (7, 3, 'Mejorar la condición física general');
INSERT INTO PROTOCOL_OBJECTIVE (ID_PROTOCOL_OBJECTIVE, ID_PROTOCOL, DESCRIPTION) VALUES (8, 3, 'Aumentar la capacidad cardiorrespiratoria');
INSERT INTO PROTOCOL_OBJECTIVE (ID_PROTOCOL_OBJECTIVE, ID_PROTOCOL, DESCRIPTION) VALUES (9, 3, 'Incrementar la fuerza y resistencia muscular');

-- Nivel 4
INSERT INTO PROTOCOL_OBJECTIVE (ID_PROTOCOL_OBJECTIVE, ID_PROTOCOL, DESCRIPTION) VALUES (10, 4, 'Mejorar el rendimiento físico');
INSERT INTO PROTOCOL_OBJECTIVE (ID_PROTOCOL_OBJECTIVE, ID_PROTOCOL, DESCRIPTION) VALUES (11, 4, 'Incrementar la capacidad cardiorrespiratoria');
INSERT INTO PROTOCOL_OBJECTIVE (ID_PROTOCOL_OBJECTIVE, ID_PROTOCOL, DESCRIPTION) VALUES (12, 4, 'Desarrollar fuerza y resistencia avanzada');

-- =========================================
-- EXERCISES - NIVEL 1
-- =========================================
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (1, 1, 'Caminar', 'Caminar a un ritmo relajado en terreno plano, con una velocidad aproximada de 10-12 min/km, respiración estable y sin fatiga.', 1, 1, 1);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (2, 1, 'Tai Chi', 'Ejercicio de movimientos lentos y fluidos, enfocados en la respiración y el control postural, sin impacto en las articulaciones.', 1, 1, 2);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (3, 1, 'Ciclismo recreativo', 'Pedaleo suave en una bicicleta, en terreno plano, a una velocidad de 10-15 km/h sin sentir fatiga en piernas ni respiración agitada.', 1, 1, 1);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (4, 1, 'Yoga dinámico', 'Secuencia fluida de posturas de yoga con respiración controlada, enfocada en la movilidad articular y el estiramiento sin fatiga.', 1, 1, 3);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (5, 1, 'Baile social', 'Bailes recreativos como salsa, merengue o bachata, sin movimientos bruscos ni intensos, manteniendo un ritmo estable.', 1, 1, 2);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (6, 1, 'Sentadilla con peso ligero', 'Sentadilla con una carga menor al 50% del 1RM, manteniendo control del movimiento y postura.', 2, 2, 4);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (7, 1, 'Press de banca con mancuernas', 'Levantar mancuernas ligera acostado en un banco, controlando el descenso y la extensión de brazos.', 2, 2, 4);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (8, 1, 'Peso muerto con barra vacia','Levantar una barra sin discos, enfocándose en la técnica y el rango de movimiento', 2, 2, 5);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (9, 1, 'Elevaciones laterales','Levantar mancuernas pequeñas hasta la altura de los hombros con un movimiento controlado.', 2, 2, 5);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (10, 1, 'Curl de biceps con bandas','Utilizar una banda elástica para realizar flexiones de codo de forma controlada.', 2, 2, 6);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (11, 1, 'Caminar sobre una linea','Caminar en línea recta colocando un pie justo delante del otro, manteniendo el equilibrio.', 3, 1, 7);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (12, 1, 'Lanzamiento atrapada de pelota','Lanzar una pelota pequeña contra la pared o a otra persona y atraparla con una sola mano', 3, 1, 7);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (13, 1, 'Desplazamientos laterales suaves','Caminar lateralmente con pasos cortos, manteniendo la espalda recta y la mirada al frente.', 3, 1, 7);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (14, 1, 'Golpes suaves con los pies (dribbling estático)','Controlar un balón con el interior y exterior del pie sin levantarlo del suelo, en posición estática.', 3, 1, 7);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (15, 1, 'Toques alternos de la mano','En posición sentada o de pie, tocar con la mano derecha la rodilla izquierda y viceversa en un ritmo lento.', 3, 1, 7);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (16, 1, 'Estiramiento de espala baja','Acostado boca arriba, llevar ambas rodillas al pecho y abrazarlas suavemente durante 20-30 segundos.', 4, 1, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (17, 1, 'Estiramiento de isquiotibiales','De pie, colocar un pie sobre una superficie elevada y flexionar el torso hacia adelante sin forzar. Mantener 20-30 segundos por pierna.', 4, 1, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (18, 1, 'Estiramiento de hombros','Sentado o de pie, inclinar suavemente la cabeza hacia un lado, manteniendo la posición 20-30 segundos por lado.', 4, 1, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (19, 1, 'Estiramiento de cuello','Sentado o de pie, inclinar suavemente la cabeza hacia un lado, manteniendo la posición 20-30 segundos por lado.', 4, 1, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (20, 1, 'Postura del niño (Yoga)','Sentado sobre los talones, inclinar el torso hacia adelante con los brazos estirados y la frente tocando el suelo. Mantener 20-30 segundos.', 4, 1, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (21, 1, 'Caminata a paso lento','Trotar, concentrándose en la respiración y manteniendo una postura erguida. disminuir la frecuencia cardiaca.', 5, 1, 9);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (22, 1, 'Caminata a paso lento','Caminar enfocado en la respiración y manteniendo una postura erguida. Ideal para bajar progresivamente la frecuencia cardiaca', 5, 1, 9);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (23, 1, 'Respiración diafragmática','Levantar mancuernas ligeras acostado en un banco, la extensión de brazos.', 5, 1, 9);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (24, 1, 'Movilización de cuello','Levantar mancuernas ligeras acostado en un banco, la extensión de brazos.', 5, 1, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (25, 1, 'Foam rolling en espalda y piernas','Usa un rodillo de espuma para masajear la parte baja de la espalda, muslos y pantorrillas. Realiza movimientos lentos y controlados.', 5, 1, 10);

-- =========================================
-- EXERCISES - NIVEL 2
-- ACTIVIDAD FÍSICA LEVE (PROGRESIÓN)
-- =========================================
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (26, 2, 'Caminar', 'Caminar a un ritmo relajado en terreno plano, con una velocidad aproximada de 10-12 min/km, respiración estable y sin fatiga.', 1, 1, 1);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (27, 2, 'Tai Chi', 'Ejercicio de movimientos lentos y fluidos, enfocados en la respiración y el control postural, sin impacto en las articulaciones.', 1, 1, 2);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (28, 2, 'Ciclismo recreativo', 'Pedaleo suave en una bicicleta, en terreno plano, a una velocidad de 10-15 km/h sin sentir fatiga en piernas ni respiración agitada.', 1, 1, 1);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (29, 2, 'Yoga dinámico', 'Secuencia fluida de posturas de yoga con respiración controlada, enfocada en la movilidad articular y el estiramiento sin fatiga.', 1, 1, 3);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (30, 2, 'Baile social', 'Bailes recreativos como salsa, merengue o bachata, sin movimientos bruscos ni intensos, manteniendo un ritmo estable.', 1, 1, 2);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (31, 2, 'Sentadilla con peso ligero', 'Sentadilla con una carga menor al 50% del 1RM, manteniendo control del movimiento y postura.', 2, 2, 4);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (32, 2, 'Press de banca con mancuernas', 'Levantar mancuernas ligera acostado en un banco, controlando el descenso y la extensión de brazos.', 2, 2, 4);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (33, 2, 'Peso muerto con barra vacia','Levantar una barra sin discos, enfocándose en la técnica y el rango de movimiento', 2, 2, 5);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (34, 2, 'Elevaciones laterales','Levantar mancuernas pequeñas hasta la altura de los hombros con un movimiento controlado.', 2, 2, 5);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (35, 2, 'Curl de biceps con bandas','Utilizar una banda elástica para realizar flexiones de codo de forma controlada.', 2, 2, 6);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (36, 2, 'Caminar sobre una linea','Caminar en línea recta colocando un pie justo delante del otro, manteniendo el equilibrio.', 3, 1, 7);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (37, 2, 'Lanzamiento atrapada de pelota','Lanzar una pelota pequeña contra la pared o a otra persona y atraparla con una sola mano', 3, 1, 7);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (38, 2, 'Desplazamientos laterales suaves','Caminar lateralmente con pasos cortos, manteniendo la espalda recta y la mirada al frente.', 3, 1, 7);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (39, 2, 'Golpes suaves con los pies (dribbling estático)','Controlar un balón con el interior y exterior del pie sin levantarlo del suelo, en posición estática.', 3, 1, 7);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (40, 2, 'Toques alternos de la mano','En posición sentada o de pie, tocar con la mano derecha la rodilla izquierda y viceversa en un ritmo lento.', 3, 1, 7);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (41, 2, 'Estiramiento de espala baja','Acostado boca arriba, llevar ambas rodillas al pecho y abrazarlas suavemente durante 20-30 segundos.', 4, 1, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (42, 2, 'Estiramiento de isquiotibiales','De pie, colocar un pie sobre una superficie elevada y flexionar el torso hacia adelante sin forzar. Mantener 20-30 segundos por pierna.', 4, 1, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (43, 2, 'Estiramiento de hombros','Sentado o de pie, inclinar suavemente la cabeza hacia un lado, manteniendo la posición 20-30 segundos por lado.', 4, 1, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (44, 2, 'Estiramiento de cuello','Sentado o de pie, inclinar suavemente la cabeza hacia un lado, manteniendo la posición 20-30 segundos por lado.', 4, 1, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (45, 2, 'Postura del niño (Yoga)','Sentado sobre los talones, inclinar el torso hacia adelante con los brazos estirados y la frente tocando el suelo. Mantener 20-30 segundos.', 4, 1, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (46, 2, 'Caminata a paso lento','Trotar, concentrándose en la respiración y manteniendo una postura erguida. disminuir la frecuencia cardiaca.', 5, 1, 9);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (47, 2, 'Caminata a paso lento','Caminar enfocado en la respiración y manteniendo una postura erguida. Ideal para bajar progresivamente la frecuencia cardiaca', 5, 1, 9);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (48, 2, 'Respiración diafragmática','Levantar mancuernas ligeras acostado en un banco, la extensión de brazos.', 5, 1, 9);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (49, 2, 'Movilización de cuello','Levantar mancuernas ligeras acostado en un banco, la extensión de brazos.', 5, 1, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (50, 2, 'Foam rolling en espalda y piernas','Usa un rodillo de espuma para masajear la parte baja de la espalda, muslos y pantorrillas. Realiza movimientos lentos y controlados.', 5, 1, 10);

-- =========================================
-- EXERCISES - NIVEL 3
-- =========================================
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (51, 3, 'Trotar', 'Carrera a ritmo moderado (5:30-6:30 min/km), con respiración controlada, sin sensación de fatiga extrema y con capacidad de hablar.', 1, 3, 2);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (52, 3, 'Ciclismo en terreno ondulado', 'Pedaleo en bicicleta a 15-20 km/h con algunas subidas y bajadas moderadas, manteniendo un esfuerzo estable', 1, 3, 1);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (53, 3, 'Senderismo', 'Caminar en un terreno con inclinaciones leves y moderadas, a un ritmo constante, sintiendo esfuerzo en las piernas y ligera sudoración.', 1, 3, 11);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (54, 3, 'Natacion recreativa', 'Nado continuo estilo libre o espalda, a ritmo moderado, sin pausas prolongadas y con una respiración estable.', 1, 3, 2);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (55, 3, 'Baile intensivo', 'Bailes como zumba, hip hop o aeróbicos con movimientos continuos, cambios de ritmo y mayor demanda cardiovascular.', 1, 3, 3);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (56, 3, 'Sentailla con peso ligero', 'Sentadilla con una carga menor al 50% del 1RM, manteniendo control del movimiento y postura.', 2, 4, 4);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (57, 3, 'Press de banca con mancuernas', 'Levantar mancuernas ligeras acostado en un banco, controlando el descenso y la extensión de brazos.', 2, 4, 4);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (58, 3, 'Peso muerto con barra vacia', 'Levantar una barra sin discos, enfocándose en la técnica y el rango de movimiento.', 2, 4, 5);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (59, 3, 'Elevaciones laterales', 'Levantar mancuernas pequeñas hasta la altura de los hombros con un movimiento controlado.', 2, 4, 5);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (60, 3, 'Curlp de biceps con bandas', 'Utilizar una banda elástica para realizar flexiones de codo de forma controlada.', 2, 4, 5);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (61, 3, 'Escalera de coordinación', 'Realizar patrones de pisada dentro de una escalera de agilidad en el suelo (ejemplo: dos pies dentro, dos fuera).', 3, 3, 12);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (62, 3, 'Saltos con cambio de direccion', 'Realizar pequeños saltos en un pie mientras se gira 90° en cada repetición.', 3, 3, 12);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (63, 3, 'Pase con diferentes superficies', 'Pasar un balón usando distintas partes del cuerpo (muslo, pie, cabeza) sin que toque el suelo.', 3, 3, 12);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (64, 3, 'Trote con ritmo variable', 'Correr a ritmo controlado cambiando la velocidad cada 5-10 segundos sin perder el equilibrio.', 3, 3, 12);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (65, 3, 'Rebote de pelotas con variaciones', 'Hacer botar una pelota alternando la altura y la velocidad del bote sin perder el control.', 3, 3, 12);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (66, 3, 'Estiramiento dinamico de piernas', 'De pie, balancear una pierna hacia adelante y atrás, aumentando progresivamente la amplitud. Repetir 10-15 veces por pierna.', 4, 3, 12);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (67, 3, 'Puente de gluteos con estiramiento', 'Acostado boca arriba, elevar la cadera manteniendo los glúteos contraídos y extender una pierna recta hacia el techo. Mantener 15 segundos por lado.', 4, 3, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (68, 3, 'Estiramiento de cuádriceps en posición de zancada', 'Desde una posición de estocada, bajar la rodilla trasera al suelo y llevar la pelvis hacia adelante sin arquear la espalda. Mantener 20-30 segundos por pierna.', 4, 3, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (69, 3, 'Flexion lateral de tronco', 'De pie con los pies juntos, inclinar el torso lateralmente llevando un brazo por encima de la cabeza. Mantener 20-30 segundos por lado.', 4, 3, 8);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (70, 3, 'Torsion espinal en posicion sentada', 'Sentado con una pierna cruzada sobre la otra, girar el torso hacia el lado de la pierna flexionada usando el brazo opuesto para apoyarse.', 4, 3, 13);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (71, 3, 'Trote suave', 'Corre a un ritmo muy cómodo, permitiendo que la frecuencia cardiaca disminuya progresivamente. Respira de manera relajada.', 5, 3, 14);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (72, 3, 'Relajación muscular progresiva de Jacobson', 'Acuéstate y tensa distintos grupos musculares, desde los pies hasta la cabeza. Mantén la tensión por 5 seg y luego relaja. Repite el proceso en todo el cuerpo.', 5, 3, 7);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (73, 3, 'Movilizcion dinamica de caderas', 'En posición de zancada, mueve la cadera en pequeños círculos para liberar tensión. Alterna entre ambos lados.', 5, 3, 15);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (74, 3, 'Estiramiento dinamico de piernas', 'Balancea una pierna adelante y atrás mientras te sostienes de un soporte. Mantén el control del movimiento. Cambia de pierna.', 5, 3, 16);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (75, 3, 'Foam rolling en cuádriceps e isquiotibiales', 'Acuéstate boca abajo y coloca un foam roller bajo los muslos. Rueda lentamente, concentrándote en las zonas tensas.', 5, 3, 10);

-- =========================================
-- EXERCISES - NIVEL 4
-- =========================================
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (76, 4, 'Carrera continua', 'Correr a ritmo alto (3:30 - 4:30 min/km) con respiración acelerada, sensación de esfuerzo sostenido y sin capacidad de hablar fluidamente.', 1, 5, 17);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (77, 4, 'Ciclismo de ruta', 'Pedaleo intenso en terreno variado, con subidas y descensos, a velocidad de 25-35 km/h con alta exigencia muscular y respiratoria.', 1, 5, 11);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (78, 4, 'Natacion de competencia', 'Nado continuo con velocidad alta (estilo libre o mariposa), con respiración controlada y alta demanda cardiovascular.', 1, 5, 2);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (79, 4, 'Hit aerobico', 'Entrenamiento de intervalos con 30 seg de ejercicio de alta intensidad (saltos, burpees, sprints) y 30 seg de descanso.', 1, 5, 17);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (80, 4, 'Saltos con cuerda', 'Saltar la cuerda a ritmo rápido y constante, con variaciones como saltos dobles o alternados, manteniendo un esfuerzo sostenido.', 1, 5, 18);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (81, 4, 'Sentadilla con barra', 'Realizar sentadillas con peso medio, controlando la técnica y manteniendo la espalda recta.', 2, 6, 19);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (82, 4, 'Press de banca con barra', 'Levantar una barra con peso moderado hasta la extensión completa de los codos.', 2, 6, 19);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (83, 4, 'Peso muerto', 'Levantar una carga moderada desde el suelo hasta la cadera manteniendo la espalda recta.', 2, 6, 19);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (84, 4, 'Remo con barra', 'Con el torso inclinado, tirar de la barra hacia el abdomen manteniendo los codos cerca del cuerpo.', 2, 6, 19);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (85, 4, 'Press militar con Barra', 'Elevar la barra desde los hombros hasta la extensión completa de los brazos.', 2, 6, 19);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (86, 4, 'Saltos en patron de escalera', 'Saltar rápidamente dentro y fuera de una escalera de agilidad siguiendo diferentes patrones.', 3, 5, 17);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (87, 4, 'Dribbling con cambios de ritmo', 'Conducir un balón alternando velocidad y dirección sin perder el control.', 3, 5, 17);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (88, 4, 'Carreras con cambio de direccion rapido', 'Correr en zigzag entre conos o marcas en el suelo, manteniendo el equilibrio y la velocidad.', 3, 5, 17);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (89, 4, 'Salto a pies alternos con ritmo', 'Saltar alternando pies mientras se sigue el ritmo de una señal sonora o visual.', 3, 5, 17);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (90, 4, 'Tocar conos o marcas con reaccion rapida', 'Realizar desplazamientos laterales tocando conos o marcas en el suelo según indicaciones aleatorias.', 3, 5, 17);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (91, 4, 'Postura de la paloma (Yoga)', 'Desde posición de plancha, llevar una pierna flexionada al frente y extender la otra hacia atrás, inclinando el torso hacia adelante. ', 4, 5, 20);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (92, 4, 'Estiramiento de isquiotibiales en posición de pie', 'De pie, bajar lentamente el torso tratando de tocar los dedos de los pies sin flexionar las rodillas. ', 4, 5, 21);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (93, 4, 'Espagat (Split)', 'Desde una posición de zancada, bajar progresivamente la pelvis hasta que ambas piernas estén extendidas en línea recta. ', 4, 5, 20);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (94, 4, 'Estiramiento de flexores de cadera avanzado', 'Desde posición de estocada profunda, apoyar ambas manos en el suelo y empujar la cadera hacia adelante. ', 4, 5, 21);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (95, 4, 'Puente de espalda (Bridge)', 'Acostado boca arriba, colocar las manos y los pies en el suelo y elevar la cadera hasta formar un arco con el cuerpo.', 4, 5, 22);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (96, 4, 'Trote progesivo', 'Comienza con un trote suave y aumenta ligeramente la velocidad sin llegar a esfuerzo máximo. Mantén una respiración controlada', 5, 5, 23);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (97, 4, 'Postura del perro boca abajo (yoga)', 'Desde posición de plancha, levanta las caderas formando una “V” invertida. Mantén los talones en el suelo y estira la espalda.', 5, 5, 24);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (98, 4, 'Foam rolling profundo en glúteos y espalda', 'Usa el rodillo en zonas más profundas como los glúteos y la parte superior de la espalda. Mantén presión en puntos de tensión.', 5, 5, 9);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (99, 4, 'Movilización dinámica de hombros y brazos', 'Realiza círculos amplios con los brazos y balanceos controlados para relajar los hombros después del ejercicio.', 5, 5, 25);
INSERT INTO EXERCISE (ID_EXERCISE, ID_PROTOCOL, NAME, EXERCISE_DESCRIPTION, ID_ACTIVITY_TYPE, ID_INTENSITY_TYPE, ID_DURATION) VALUES (100, 4, 'Movilizaciónde columna con gato vaca (yoga)', 'En cuadrupedia, alterna entre arquear la espalda hacia arriba y hundir el abdomen, sincronizando el movimiento con la respiración.', 5, 5, 26);