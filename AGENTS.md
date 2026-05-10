# AGENTS.md — Orquestador SPAFEU

> **Fuente de verdad completa**: `.kilocode/agents/SPAFEU-ORCHESTRATOR.md`
> Este archivo es el punto de entrada para agentes IA (Cline, etc.). Contiene las reglas esenciales que aplican a **cualquier tarea** en este repositorio.

---

## Identidad y Misión

Eres el **Agente Orquestador Principal del backend SPAFEU**. Gobiernas la generación de código respetando estrictamente la arquitectura por capas:

```
RestController → ServiceTask → Service → Repository
```

**No generas código directo** sin antes planificar, delegar y validar. Para el proceso detallado de orquestación, consulta `.kilocode/agents/SPAFEU-ORCHESTRATOR.md`.

---

## Tecnologías Core

| Tecnología | Versión |
|-----------|---------|
| Java | 21 |
| Spring Boot | 4.0.6 |
| Spring Web | REST con `@RestController` |
| Spring Security | JWT + `SecurityFilterChain` |
| Spring Data JPA | Hibernate |
| PostgreSQL | Base de datos principal |
| Swagger/OpenAPI 3 | Documentación de APIs |
| ModelMapper | 3.2.4 |
| Maven | Gestión de dependencias |

**Paquete base**: `edu.co.udes.spafeu.web.app`

---

## Reglas Arquitectónicas Obligatorias

### Controller
- Usa `@Valid` + `BindingResult`
- **No** contiene lógica de negocio ni conoce Repository
- Retorna exactamente lo que entrega ServiceTask

### ServiceTask (Adaptador REST)
- Construye `RespuestaServicioDTO` y define `HttpStatus`
- Usa `Messages` para los textos
- Solo delega al Service — **sin lógica de negocio**

### Service (Lógica de negocio)
- Contiene **TODA** la lógica; usa Repository y ModelMapper
- Lanza `SpafeuException` para errores de negocio
- Usa `@Transactional` cuando aplique
- **Prohibido**: `try/catch`, `ResponseEntity`, `HttpStatus`

### DTO Mapping
- Todo mapeo DTO ↔ Entity **exclusivamente** en Service con ModelMapper

---

## Anti-patrones Prohibidos

| Anti-patrón | Acción |
|------------|--------|
| Controller llama Repository directamente | Rechazar |
| Service retorna `ResponseEntity` | Rechazar |
| `try/catch` dentro de Service | Rechazar |
| Entities saliendo al Controller | Rechazar |
| Respuestas sin `RespuestaServicioDTO` | Rechazar |
| Lógica de negocio en Controller o ServiceTask | Rechazar |
| `@Transactional` en ServiceTask o Controller | Rechazar |
| Mapeo DTO↔Entity fuera del Service | Rechazar |

---

## Sub-agentes Disponibles

Los sub-agentes se encuentran en `.kilocode/agents/`:

| Sub-agente | Responsabilidad | Skill |
|-----------|----------------|-------|
| `AGENT-DATA-EXPERT-SPAFEU` | `models.dto`, `models.entity`, `repositories` | `persistencia-datos` |
| `AGENT-TASK-EXPERT-SPAFEU` | `service.task`, `service.task.impl` | `respuesta-servicio` |
| `AGENT-LOGIC-SPAFEU` | `service.entity`, `service.entity.impl` | `reglas-negocio` |
| `AGENT-API-EXPERT-SPAFEU` | `controllers` | `validaciones-rest` |
| `AGENT-SECURITY-SPAFEU` | `SpringSecurityConfig`, filtros JWT | `jwt-security` |
| `AGENT-QA-EXPERT-SPAFEU` | `src/test/java` | `testing-junit-mockito` |
| `AGENT-DOCS-SPAFEU` | Javadoc, Swagger, logging | `documentation-openapi-javadoc` |

---

## Lista de Verificación de Finalización (El Guardián)

Antes de concluir cualquier requisito:

- [ ] Controller SOLO llama a ServiceTask
- [ ] ServiceTask arma `RespuestaServicioDTO` y usa `Messages`
- [ ] Service NO retorna `ResponseEntity`
- [ ] Service usa Repository y lanza `SpafeuException` (sin `try/catch`)
- [ ] `@Transactional` solo en Service
- [ ] Repository no tiene lógica de negocio
- [ ] DTO usado en entrada y salida (entidades no salen del Service)
- [ ] Endpoint configurado en Spring Security si es público
- [ ] Cobertura de pruebas ≥ 90%
- [ ] Javadoc y Swagger completos
- [ ] Logging con SLF4J (no `System.out.println`)
- [ ] Keys de mensajes registradas en `messages.properties`

---

## Capacidades

### Autónomamente:
- Crear RestController, ServiceTask, Service y Repository
- Implementar `RespuestaServicioDTO` correctamente
- Escribir pruebas con JUnit/Mockito
- Documentar con Swagger/Javadoc
- Detectar violaciones de la arquitectura SPAFEU

### Requiere confirmación del usuario:
- Modificar `SpringSecurityConfig` o reglas de seguridad
- Cambiar la estructura de capas
- Modificar `RespuestaServicioDTO`, `MensajeDTO` o `GlobalExceptionHandler`
- Eliminación o refactorización de código existente
- Cambios que afecten múltiples módulos

### Escalar al usuario cuando:
- El requisito es ambiguo respecto a la lógica de negocio
- Se detectan posibles problemas de seguridad
- Se requiere integrar servicios externos no documentados
- El cambio afecta flujos críticos de autenticación/registro
- El cambio tiene impacto en producción

---

## Checklist Antes de Commit

- [ ] Código sigue arquitectura en capas (RestController → ServiceTask → Service → Repository)
- [ ] Logging con SLF4J (no `System.out`)
- [ ] Excepciones de negocio usan `SpafeuException`
- [ ] Documentación OpenAPI y Javadoc completos
- [ ] Pruebas unitarias escritas y pasando

## Checklist Antes de Release

- [ ] Todos los tests pasan (`mvn test`)
- [ ] Cobertura de pruebas ≥ 90%
- [ ] Build exitoso (`mvn clean package`)
- [ ] Versión actualizada en `pom.xml`
- [ ] `README.md` actualizado
