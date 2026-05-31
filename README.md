# SPAFEU Backend

<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen.svg?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg?style=for-the-badge&logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg?style=for-the-badge&logo=postgresql)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-Auth-black.svg?style=for-the-badge&logo=jsonwebtokens)](https://jwt.io/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-green.svg?style=for-the-badge&logo=swagger)](https://swagger.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](LICENSE)

**Sistema de Promoción de la Actividad Física en Estudiantes Universitarios**

Backend RESTful para la promoción, monitoreo y gestión integral de la actividad física en el contexto universitario.

[Documentación API](#documentación-api) · [Instalación](#instalación) · [Arquitectura](#arquitectura) · [Contribuir](#contribuir)

</div>

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Características Principales](#-características-principales)
- [Stack Tecnológico](#-stack-tecnológico)
- [Arquitectura](#-arquitectura)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Documentación API](#-documentación-api)
- [Seguridad](#-seguridad)
- [Testing](#-testing)
- [Contribuidores](#-contribuidores)
- [Licencia](#-licencia)

---

## 📝 Descripción

**SPAFEU** (Sistema de Promoción de la Actividad Física en Estudiantes Universitarios) es una plataforma backend diseñada para fomentar, monitorear y gestionar la actividad física en estudiantes universitarios. El sistema permite:

- **Registro y autenticación segura** de estudiantes con verificación KYC (Know Your Customer)
- **Perfiles de salud personalizados** con clasificación de nivel de actividad física
- **Protocolos de actividad física** adaptados a las necesidades de cada usuario
- **Seguimiento de sesiones de ejercicio** con métricas de progreso
- **Dashboard analítico** para visualizar el avance físico del estudiante
- **Gestión de cuestionarios** para evaluación inicial y seguimiento

El backend está construido con una **arquitectura limpia por capas**, garantizando separación de responsabilidades, escalabilidad y mantenibilidad.

---

## ✨ Características Principales

| Módulo | Descripción |
|--------|-------------|
| 🔐 **Autenticación JWT** | Sistema de autenticación stateless con tokens JWT, roles y permisos granulares |
| 👤 **Gestión de Usuarios** | Registro con validación de campus/programa, perfiles de salud y seguimiento KYC |
| 📊 **Perfiles de Salud** | Clasificación automática del nivel de actividad física basada en cuestionarios |
| 📋 **Cuestionarios** | Sistema de preguntas y respuestas para evaluación de condición física |
| 🏃 **Protocolos de Actividad** | Asignación de protocolos personalizados según perfil de salud |
| 🏋️ **Ejercicios** | Catálogo de ejercicios clasificados por tipo, intensidad y duración |
| 📈 **Seguimiento de Sesiones** | Registro y análisis de sesiones de ejercicio completadas |
| 📉 **Dashboard de Progreso** | Métricas y estadísticas del avance físico del usuario |
| 🏫 **Gestión de Sedes** | Administración de campus universitarios y programas académicos |
| 📧 **Notificaciones por Email** | Envío de correos con plantillas Thymeleaf |

---

## 🛠 Stack Tecnológico

### Core
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 21 | Lenguaje de programación |
| **Spring Boot** | 4.0.6 | Framework principal |
| **Spring Security** | 4.0.6 | Seguridad y autenticación |
| **Spring Data JPA** | 4.0.6 | Persistencia de datos |
| **Spring Validation** | 4.0.6 | Validación de datos |
| **Spring Mail** | 4.0.6 | Envío de correos electrónicos |

### Base de Datos
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **PostgreSQL** | 16+ | Base de datos relacional principal |
| **Hibernate** | 6.x | Implementación JPA |

### Seguridad
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **JJWT** | 0.13.0 | Generación y validación de tokens JWT |

### Utilidades
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **ModelMapper** | 3.2.4 | Mapeo DTO ↔ Entity |
| **Lombok** | 1.18.46 | Reducción de boilerplate |
| **Thymeleaf** | 4.0.6 | Plantillas de correo electrónico |
| **Jackson** | 2.x | Serialización JSON |

### Documentación
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **SpringDoc OpenAPI** | 3.0.3 | Documentación Swagger/OpenAPI 3 |

### Testing
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **JUnit 5** | 5.x | Framework de pruebas |
| **Mockito** | 5.x | Mocking de dependencias |
| **JaCoCo** | 0.8.14 | Cobertura de código (mínimo 90%) |

---

## 🏗 Arquitectura

El proyecto sigue una **arquitectura limpia por capas** con separación estricta de responsabilidades:

```
┌─────────────────────────────────────────────────────────────┐
│                    REST Controller Layer                     │
│  - Expone endpoints REST                                     │
│  - Valida requests con @Valid + BindingResult              │
│  - NO contiene lógica de negocio                             │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                    ServiceTask Layer                         │
│  - Construye RespuestaServicioDTO                            │
│  - Define HttpStatus de respuesta                            │
│  - Usa Messages para textos internacionalizados              │
│  - Solo delega al Service                                    │
│  - NO contiene lógica de negocio                             │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                    Service Layer                             │
│  - Contiene TODA la lógica de negocio                        │
│  - Usa Repository para acceso a datos                          │
│  - Usa ModelMapper para mapeo DTO ↔ Entity                   │
│  - Lanza SpafeuException para errores de negocio             │
│  - @Transactional para transacciones                           │
│  - NO retorna ResponseEntity                                 │
│  - NO usa try/catch                                          │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                    Repository Layer                            │
│  - Solo acceso a datos con Spring Data JPA                   │
│  - NO contiene lógica de negocio                             │
└─────────────────────────────────────────────────────────────┘
```

### Principios Arquitectónicos

- ✅ **Separación de DTO y Entity**: Los DTOs se usan para comunicación con el frontend; las Entities solo en Service y Repository
- ✅ **Inmutabilidad de respuestas**: Todas las respuestas usan `RespuestaServicioDTO` estandarizado
- ✅ **Seguridad centralizada**: Configuración JWT y roles en capa de seguridad dedicada
- ✅ **Transacciones declarativas**: `@Transactional` solo en Service
- ✅ **Manejo global de excepciones**: `GlobalExceptionHandler` para respuestas consistentes

---

## 📁 Estructura del Proyecto

```
spafeu-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── edu/co/udes/spafeu/web/app/
│   │   │       ├── auth/                          # Capa de autenticación JWT
│   │   │       │   ├── filter/                    # Filtros JWT (Authentication/Validation)
│   │   │       │   └── service/                   # Servicio de generación/validación JWT
│   │   │       ├── config/                        # Configuraciones (Security, OpenAPI, App)
│   │   │       ├── controllers/                   # RestControllers (8 controladores)
│   │   │       ├── exception/                     # Excepciones y manejador global
│   │   │       ├── models/
│   │   │       │   ├── dto/                     # Data Transfer Objects
│   │   │       │   ├── entities/                  # Entidades JPA (22 entidades)
│   │   │       │   └── enums/                     # Enumeraciones y conversores
│   │   │       ├── repositories/                  # Repositorios Spring Data JPA
│   │   │       └── service/
│   │   │           ├── entity/                    # Services con lógica de negocio
│   │   │           └── task/                      # ServiceTasks (adaptadores REST)
│   │   └── resources/
│   │       ├── application.properties             # Configuración principal
│   │       └── email-asuntos.properties           # Configuración de emails
│   └── test/
│       └── java/                                  # Pruebas unitarias (JUnit 5 + Mockito)
├── pom.xml                                        # Dependencias Maven
└── README.md                                      # Este archivo
```

### Entidades Principales

- `Usuario` - Usuarios del sistema
- `UsuarioKyc` - Verificación Know Your Customer
- `UserHealthProfile` - Perfil de salud del usuario
- `PhysicalActivityProtocol` - Protocolos de actividad física
- `Exercise` - Ejercicios disponibles
- `UserExerciseSession` - Sesiones de ejercicio del usuario
- `Questionnaire` - Cuestionarios de evaluación
- `Question` / `Option` - Preguntas y opciones
- `UserAnswer` - Respuestas de usuarios
- `Campus` / `AcademicProgram` / `CampusProgram` - Sedes y programas
- `Role` - Roles de usuario
- `PasswordResetToken` - Tokens de recuperación de contraseña

---

## 🚀 Instalación

### Prerrequisitos

- **Java 21** o superior ([Descargar](https://adoptium.net/))
- **Maven 3.9+** ([Descargar](https://maven.apache.org/download.cgi))
- **PostgreSQL 16+** ([Descargar](https://www.postgresql.org/download/))
- **Git** ([Descargar](https://git-scm.com/downloads))

### 1. Clonar el repositorio

```bash
git clone https://github.com/CristianMisse96/spafeu-backend.git
cd spafeu-backend
```

### 2. Configurar la base de datos

Crear una base de datos PostgreSQL:

```sql
CREATE DATABASE spafeu_db;
CREATE USER spafeu_user WITH PASSWORD 'tu_password_seguro';
GRANT ALL PRIVILEGES ON DATABASE spafeu_db TO spafeu_user;
```

### 3. Configurar variables de entorno

Crear un archivo `application-qa.properties` en `src/main/resources/` (o usar variables de entorno):

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/spafeu_db
spring.datasource.username=spafeu_user
spring.datasource.password=tu_password_seguro

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=tu_clave_secreta_jwt_muy_segura_de_al_menos_256_bits
jwt.expiration=86400000

# Email (opcional)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=tu_email@gmail.com
spring.mail.password=tu_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 4. Compilar y ejecutar

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar tests
mvn test

# Ejecutar la aplicación
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

---

## ⚙️ Configuración

### Perfiles de Spring

El proyecto utiliza perfiles de Spring para diferentes entornos:

| Perfil | Archivo | Uso |
|--------|---------|-----|
| `qa` | `application-qa.properties` | Desarrollo y pruebas |
| `prod` | `application-prod.properties` | Producción (crear si es necesario) |

Activar un perfil:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Variables de Entorno Importantes

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | URL de conexión JDBC | `jdbc:postgresql://localhost:5432/spafeu_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de BD | `spafeu_user` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de BD | `***` |
| `JWT_SECRET` | Clave secreta JWT | `mi_clave_secreta_muy_larga...` |
| `JWT_EXPIRATION` | Tiempo de expiración JWT (ms) | `86400000` |
| `SPRING_MAIL_USERNAME` | Email para notificaciones | `spafeu@universidad.edu.co` |
| `SPRING_MAIL_PASSWORD` | Contraseña de aplicación | `***` |

---

## 📚 Documentación API

Una vez ejecutada la aplicación, la documentación Swagger/OpenAPI está disponible en:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Endpoints Principales

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| `POST` | `/api/auth/register` | Registro de nuevo usuario | Público |
| `POST` | `/api/auth/login` | Inicio de sesión | Público |
| `POST` | `/api/auth/forgot-password` | Solicitar recuperación de contraseña | Público |
| `POST` | `/api/auth/reset-password` | Restablecer contraseña | Público |
| `GET` | `/api/users/me` | Obtener perfil del usuario autenticado | JWT |
| `PUT` | `/api/users/me/health-profile` | Actualizar perfil de salud | JWT |
| `GET` | `/api/questionnaires` | Listar cuestionarios disponibles | JWT |
| `POST` | `/api/questionnaires/{id}/answers` | Enviar respuestas de cuestionario | JWT |
| `GET` | `/api/protocols` | Listar protocolos de actividad física | JWT |
| `GET` | `/api/protocols/{id}/exercises` | Obtener ejercicios de un protocolo | JWT |
| `GET` | `/api/exercises` | Listar ejercicios disponibles | JWT |
| `POST` | `/api/sessions` | Registrar nueva sesión de ejercicio | JWT |
| `GET` | `/api/sessions/progress` | Obtener dashboard de progreso | JWT |
| `GET` | `/api/campus` | Listar sedes universitarias | Público/JWT |

> **Nota**: Consulta la documentación Swagger completa para detalles de request/response de cada endpoint.

---

## 🔒 Seguridad

El sistema implementa seguridad robusta mediante:

### Autenticación JWT (Stateless)
- Tokens JWT firmados con HMAC-SHA256
- Expiración configurable (por defecto 24 horas)
- Refresh token implícito mediante re-login

### Autorización Basada en Roles
- Roles: `ADMIN`, `USER`, `MODERATOR`
- Protección de endpoints mediante `@PreAuthorize` y configuración de `SecurityFilterChain`
- Acceso granular a recursos según rol

### Protección de Datos
- Contraseñas encriptadas con BCrypt
- Validación de entrada con Jakarta Validation (`@Valid`)
- Manejo seguro de tokens de recuperación de contraseña (expiración de 1 hora)

### Configuración CORS
- Configuración de orígenes permitidos para integración con frontend

---

## 🧪 Testing

El proyecto mantiene una **cobertura de código mínima del 90%** gracias a JaCoCo.

### Ejecutar Tests

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar con cobertura (genera reporte en target/site/jacoco/)
mvn clean verify

# Ejecutar un test específico
mvn test -Dtest=NombreDelTest
```

### Reporte de Cobertura

Después de ejecutar `mvn clean verify`, el reporte de JaCoCo estará disponible en:

```
target/site/jacoco/index.html
```

Abrir en navegador para ver cobertura detallada por clase y método.

### Estrategia de Testing

- **Unit Tests**: Pruebas aisladas con Mockito para Services y ServiceTasks
- **Integration Tests**: Pruebas de controladores con `@WebMvcTest`
- **Coverage Gates**: Fallo de build si cobertura < 90%

---

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! Sigue estos pasos:

### 1. Fork y Clone

```bash
# Hacer fork en GitHub, luego:
git clone https://github.com/tu-usuario/spafeu-backend.git
cd spafeu-backend
```

### 2. Crear una Rama

```bash
git checkout -b feature/nueva-funcionalidad
# o
git checkout -b fix/correccion-bug
```

### 3. Desarrollar y Testear

- Sigue la arquitectura por capas del proyecto
- Asegúrate de que los tests pasen (`mvn test`)
- Mantén cobertura >= 90%
- Documenta con Javadoc y anotaciones Swagger

### 4. Commit y Push

```bash
git add .
git commit -m "feat(modulo): descripción clara del cambio"
git push origin feature/nueva-funcionalidad
```

> Usa [Conventional Commits](https://www.conventionalcommits.org/) para mensajes de commit.

### 5. Pull Request

Abre un Pull Request describiendo:
- Qué problema resuelve o funcionalidad agrega
- Cómo probar los cambios
- Screenshots o evidencia si aplica

### Guías de Contribución

- ✅ Respeta la arquitectura: Controller → ServiceTask → Service → Repository
- ✅ No agregues lógica de negocio en Controllers ni ServiceTasks
- ✅ Usa DTOs para comunicación con el frontend
- ✅ Lanza `SpafeuException` para errores de negocio (no try/catch en Service)
- ✅ Agrega tests para cada nueva funcionalidad
- ✅ Documenta endpoints con anotaciones OpenAPI

---

## 👥 Contribuidores

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/CristianMisse96">
        <img src="https://github.com/CristianMisse96.png?size=100" width="100px;" alt="Cristian Misse"/><br />
        <sub><b>Cristian Misse</b></sub>
      </a><br />
      <sub>💻 Lead Developer</sub><br />
      <a href="https://github.com/CristianMisse96" title="GitHub">🐙</a>
    </td>
  </tr>
</table>

### Cómo aparecer aquí

Si contribuyes al proyecto con código, documentación, reportes de bugs o ideas, ¡serás agregado a esta tabla! Solo asegúrate de:

1. Hacer al menos un PR mergeado
2. Seguir las guías de contribución
3. Mantener buena comunicación en issues y PRs

---

## 📄 Licencia

Este proyecto está licenciado bajo la **Licencia MIT**.

```
MIT License

Copyright (c) 2026 Cristian Misse

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

Consulta el archivo [LICENSE](LICENSE) para el texto completo.

---

## 📞 Contacto y Soporte

- **Issues**: [GitHub Issues](https://github.com/CristianMisse96/spafeu-backend/issues)
- **Email**: [cgmisse@gmail.com](mailto:cgmisse@gmail.com)
- **Proyecto**: [https://github.com/CristianMisse96/spafeu-backend](https://github.com/CristianMisse96/spafeu-backend)

---

<div align="center">

**[⬆ Volver al inicio](#spafeu-backend)**

Desarrollado con ❤️ para promover la actividad física en estudiantes universitarios.

</div>
