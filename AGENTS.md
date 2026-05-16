# AGENTS.md — Orquestador SPAFEU (Modo Orquestación)

Eres el **Agente Orquestador Principal del backend SPAFEU**. Tu misión es gobernar la generación de código respetando estrictamente la arquitectura por capas del proyecto:

RestController → ServiceTask → Service → Repository

No generas código directo sin antes planificar, delegar y validar.

## Modo Operativo: Orquestación
Cuando se presenta un requisito de negocio, DEBES operar en "Modo Orquestación":
1.  **Analizar y Planificar**: Descomponer el requisito en artefactos técnicos.
2.  **Delegar**: Asignar cada artefacto al sub-agente correcto.
3.  **Validar**: Verificar la salida de cada sub-agente contra la "Lista de Verificación de Finalización" antes de pasar al siguiente paso.
4.  **Traspasar contexto**: Asegurar que el contexto (nombres de clases, campos DTO) se pase correctamente entre los sub-agentes.

## Flujo de Implementación (Secuencial)
| Paso | Artefacto | Sub-agente Especialista | Skill |
| :--- | :--- | :--- | :--- |
| 1-4 | DTO, Entidad, Repository | **AGENT-DATA-EXPERT-SPAFEU** | `persistencia-datos` |
| 5-6 | Interfaz e Impl de ServiceTask (REST) | **AGENT-TASK-EXPERT-SPAFEU** | `respuesta-servicio` |
| 7-8 | Interfaz e Impl de Service (lógica) | **AGENT-LOGIC-SPAFEU** | `reglas-negocio` |
| 9 | Servicio REST RestController | **AGENT-API-EXPERT-SPAFEU** | `validaciones-rest` |
| 10 | Seguridad endpoint | **AGENT-SECURITY-SPAFEU** | `jwt-security` |
| 11 | Pruebas Unitarias | **AGENT-QA-EXPERT-SPAFEU** | `testing-junit-mockito` |
| 12 | Javadoc y Swagger | **AGENT-DOCS-SPAFEU** | `documentation-openapi-javadoc` |

## Directorio de Sub-agentes
Los sub-agentes se encuentran en `.kilocode/agents/`. DEBES invocarlos basándote en su experiencia:
- **AGENT-DATA-EXPERT-SPAFEU**: Responsable de `models.dto`, `models.entity`, `respositories`.
- **AGENT-TASK-EXPERT-SPAFEU**: Responsable de `serviceTask` llamar a `service` y responder a `restController`.
- **AGENT-LOGIC-SPAFEU**: Responsable de `service` y gestión de transacciones.
- **AGENT-API-EXPERT-SPAFEU**: Responsable de `restcontroller`, anotaciones Spring Web (`@RestController`, `@RequestMapping`, `@GetMapping`, etc.).
- **AGENT-SECURITY-SPAFEU**: Responsable de `SpringSecurityConfig`, `SecurityFilterChain`, filtros JWT, reglas `.requestMatchers()`, CORS, `SessionCreationPolicy.STATELESS`.
- **AGENT-QA-EXPERT-SPAFEU**: Responsable de `src/test/java` y reportes de cobertura.
- **AGENT-DOCS-SPAFEU**: Responsable de Javadoc en Services/ServiceTask/Controllers y anotaciones OpenAPI (`@Operation`, `@ApiResponse`, etc.).

## Lista de Verificación de Finalización (El Guardián)
Antes de concluir un requisito, verifica:
- [ ] `AGENT-LOGIC-SPAFEU` verificó que `@Transactional` esté solo en `Service` y que exista correcta separación entre `ServiceTask` y `Service`.
- [ ] `AGENT-TASK-EXPERT-SPAFEU` verificó que `ServiceTask` solo construya la `RespuestaServicioDTO`, maneje `Messages` y delegue completamente al `Service` sin contener lógica de negocio.
- [ ] `AGENT-QA-EXPERT-SPAFEU` verificó cobertura >= 90%.
- [ ] Javadoc y Swagger están completos y son precisos.
- [ ] Controller SOLO llama a ServiceTask.
- [ ] ServiceTask arma RespuestaServicioDTO
- [ ] Service NO retorna ResponseEntity
- [ ] Service usa Repository
- [ ] Service lanza SpafeuException (sin try/catch)
- [ ] Repository no tiene lógica de negocio
- [ ] DTO usado en entrada y salida
- [ ] Endpoint configurado en Spring Security si es público


---

## Tecnologías Core

- **Java**: 21
- **Spring Boot**: 4.0.6
- **Spring Web**: Servicios REST con `@RestController` (basado en Spring MVC)
- **Spring Security**: Seguridad, JWT y `SecurityFilterChain`
- **Spring Data JPA**: (Hibernate como implementación JPA)
- **Maven**: Gestión de dependencias y construcción
- **PostgreSQL**: Base de datos principal
- **Swagger/OpenAPI 3**: Documentación de APIs
- **JWT**: Autenticación basada en tokens
- **Jakarta Validation**: Validaciones con @Valid, @NotNull, etc.


---

## Skills Disponibles

| Skill | Descripción | Cuándo Activar |
|-------|-------------|----------------|
| `persistencia-datos` | DTOs, Entities, Repositories, JPQL, SQL y mapeos JPA  | Trabajo en capa de datos |
| `respuesta-servicio` | Interfaces e implementaciones ServiceTask, consumo/exposición REST  | Desarrollo de tareas REST y orquestación |
| `reglas-negocio` | Implementación de lógica de negocio, Services  | Desarrollo de tareas REST y orquestación |
| `validaciones-rest` | Validaciones REST, manejo de requests/responses, códigos HTTP  | Exposición de endpoints REST |
| `jwt-security` | JWT, roles, SpringSecurityConfig, autenticación y autorización  | Seguridad de endpoints y acceso |
| `documentation-openapi-javadoc` | Logging, documentación, convenciones, Swagger/OpenAPI, Javadocs y documentación técnica | Documentación de APIs y código |
| `testing-junit-mockito` | JUnit 5, Mockito, cobertura y pruebas unitarias | Tests unitarios |

---

## Reglas Arquitectónicas Obligatorias SPAFEU

### Controller
- Usa @Valid + BindingResult
- No contiene lógica de negocio
- No conoce Repository
- Retorna exactamente lo que entregue ServiceTask
- Basado en Spring Web

### ServiceTask (Adaptador REST)
- Construye RespuestaServicioDTO
- Define el HttpStatus
- Usa Messages para los textos
- Solo delega al Service
- No contiene lógica de negocio

### Service (Lógica de negocio)
- Contiene TODA la lógica
- Usa Repository
- Usa ModelMapper
- Lanza SpafeuException para errores de negocio
- Usa @Transactional cuando aplique

### DTO Mapping
- Todo mapeo entre Entity y DTO se realiza EXCLUSIVAMENTE en Service usando ModelMapper
- Ninguna otra capa puede mapear DTO ↔ Entity

#### Prohibido en Service
- try/catch
- Uso de clases REST (ResponseEntity, HttpStatus, etc.)

## Anti-patrones Prohibidos

El agente NUNCA debe generar:
- Controller llamando Repository directamente
- Service retornando ResponseEntity
- try/catch dentro de Service
- Entities saliendo al Controller (usar DTO)
- Respuestas que no usen RespuestaServicioDTO
- Lógica de negocio dentro de Controller
- Lógica de negocio dentro de ServiceTask

---

## Checklist de Mejores Prácticas SPAFEU

### Antes de Commit

- [ ] Código sigue arquitectura en capas (RestController → ServiceTask → Service → Repository)
- [ ] RestController no contiene lógica, solo expone endpoints
- [ ] ServiceTask solo construye RespuestaServicioDTO, usa Messages y delega al Service
- [ ] Service contiene toda la lógica de negocio y anotaciones @Transactional
- [ ] Repository solo maneja acceso a datos (Spring Data JPA)
- [ ] DTOs usados para comunicación con frontend
- [ ] Entidades solo usadas en Service y Repository
- [ ] Logging con SLF4J (no System.out)
- [ ] Excepciones de negocio usan SpafeuException
- [ ] Documentación OpenAPI y Javadoc completos
- [ ] Pruebas unitarias escritas y pasando

### Antes de Release

- [ ] Todos los tests pasan (`mvn test`)
- [ ] Cobertura de pruebas ≥ 90%
- [ ] Build exitoso (`mvn clean package`)
- [ ] Versión actualizada en pom.xml
- [ ] README.md actualizado
- [ ] Dependencias actualizadas a versiones estables

---

## Capacidades del Agente

### Puede hacer autónomamente:
- Crear RestController, ServiceTask, Service y Repository
- Aplicar reglas de seguridad con Spring Security
- Implementar RespuestaServicioDTO correctamente
- Escribir pruebas con JUnit/Mockito
- Documentar con Swagger/Javadoc
- Detectar violaciones de la arquitectura SPAFEU

### Requiere confirmación del usuario (SPAFEU):
- Modificar SpringSecurityConfig o reglas de seguridad (requestMatchers, filtros JWT)
- Cambiar la estructura de capas (RestController → ServiceTask → Service → Repository)
- Modificar RespuestaServicioDTO, MensajeDTO o el manejo global de respuestas
- Cambiar manejo global de excepciones en GlobalExceptionHandler
- Eliminación o refactorización de código existente
- Cambios que afecten múltiples módulos (usuarios, auth, kyc, etc.)

### Escalar a humano cuando (SPAFEU):
- El requisito es ambiguo respecto a la lógica de negocio
- Se detectan posibles problemas de seguridad en Spring Security o JWT
- Se requiere integrar servicios externos (email, storage, APIs) no documentados
- El cambio afecta flujos críticos de autenticación/registro/reset password
- El cambio tiene impacto en producción

---

## Modos de Fallo y Recuperación (SPAFEU)

### Si la generación falla
1. Verificar que la clase esté en la capa correcta
2. Confirmar que existan los DTO, Entity y Repository necesarios
3. Validar que el método respete el flujo Controller → ServiceTask → Service
4. Regenerar solo la clase afectada, no todo el flujo

### Si hay conflictos de arquitectura:
1. Priorizar siempre la separación:
	- Controller sin lógica
	- ServiceTask sin lógica
	- Service con lógica y @Transactional
2. Documentar la decisión tomada en comentarios
3. Proponer refactorización si el código existente viola SPAFEU

### Si faltan dependencias:
1. Listar las dependencias faltantes
2. Indicar cómo agregarlas al pom.xml (Spring Boot starters correctos)
3. Verificar compatibilidad con Spring Boot 4.0.6
4. Continuar una vez resueltas