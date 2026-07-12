package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UploadFileServiceImpl")
@Log4j2
class UploadFileServiceImplTest {

    private UploadFileServiceImpl uploadFileService;

    @TempDir
    Path tempDir;

    private String userHomeOriginal;

    private static final String DIRECTORIO = "spafeu-uploads";
    private static final String NOMBRE_FOTO = "foto-perfil.jpg";

    @BeforeEach
    void setUp() {
        uploadFileService = new UploadFileServiceImpl();
        // Redirigir user.home al directorio temporal para aislar las pruebas de archivos
        userHomeOriginal = System.getProperty("user.home");
        System.setProperty("user.home", tempDir.toString());
    }

    @AfterEach
    void tearDown() {
        // Restaurar el user.home original
        System.setProperty("user.home", userHomeOriginal);
    }

    // ──────────────────────────────────────────────
    // cargar()
    // ──────────────────────────────────────────────

    @Nested
    @DisplayName("cargar()")
    class Cargar {

        @Test
        @DisplayName("Debe retornar Resource existente cuando el archivo existe en el directorio")
        void debeRetornarResourceCuandoArchivoExiste() throws Exception {
            // Given
            Path directorioPath = tempDir.resolve(DIRECTORIO);
            Files.createDirectories(directorioPath);
            Path archivoPath = directorioPath.resolve(NOMBRE_FOTO);
            Files.writeString(archivoPath, "contenido de prueba");

            // When
            Resource resource = uploadFileService.cargar(NOMBRE_FOTO, DIRECTORIO);

            // Then
            assertThat(resource).isNotNull();
            assertThat(resource.exists()).isTrue();
            assertThat(resource.isReadable()).isTrue();
            assertThat(resource.getFilename()).isEqualTo(NOMBRE_FOTO);
            assertThat(resource.contentLength()).isPositive();
            log.info("Test cargar archivo existente: resource obtenido correctamente");
        }

        @Test
        @DisplayName("Debe retornar Resource no existente cuando el archivo no existe")
        void debeRetornarResourceCuandoArchivoNoExiste() throws Exception {
            // Given
            String nombreFotoInexistente = "no-existe.jpg";

            // When
            Resource resource = uploadFileService.cargar(nombreFotoInexistente, DIRECTORIO);

            // Then
            assertThat(resource).isNotNull();
            assertThat(resource.exists()).isFalse();
            assertThat(resource.getFilename()).isEqualTo(nombreFotoInexistente);
            log.info("Test cargar archivo inexistente: resource no existe, filename correcto");
        }

        @Test
        @DisplayName("Debe construir la ruta usando user.home + directorio + nombreFoto")
        void debeConstruirRutaCorrectamente() throws Exception {
            // When
            Resource resource = uploadFileService.cargar(NOMBRE_FOTO, DIRECTORIO);

            // Then
            String uri = resource.getURI().toString();
            assertThat(uri).contains(tempDir.toString().replace("\\", "/"));
            assertThat(uri).contains(DIRECTORIO);
            assertThat(uri).contains(NOMBRE_FOTO);
            log.info("Test ruta construida: URI = {}", uri);
        }
    }

    // ──────────────────────────────────────────────
    // copiar()
    // ──────────────────────────────────────────────

    @Nested
    @DisplayName("copiar()")
    class Copiar {

        @Mock
        private MultipartFile archivo;

        private static final UUID UUID_FIJO = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        @Test
        @DisplayName("Debe copiar archivo exitosamente y retornar nombre con UUID + nombre original")
        void debeCopiarArchivoExitosamente() throws Exception {
            // Given
            String originalFilename = "foto.jpg";
            byte[] contenido = "contenido de prueba para el archivo".getBytes();

            when(archivo.getOriginalFilename()).thenReturn(originalFilename);
            when(archivo.getInputStream()).thenReturn(new ByteArrayInputStream(contenido));

            try (MockedStatic<UUID> uuidMock = mockStatic(UUID.class, CALLS_REAL_METHODS)) {
                uuidMock.when(UUID::randomUUID).thenReturn(UUID_FIJO);

                // When
                String nombreResultado = uploadFileService.copiar(archivo, DIRECTORIO);

                // Then
                String expectedName = UUID_FIJO + "_" + originalFilename;
                assertThat(nombreResultado).isEqualTo(expectedName);

                Path archivoCreado = tempDir.resolve(DIRECTORIO).resolve(expectedName);
                assertThat(archivoCreado).exists();
                assertThat(Files.readAllBytes(archivoCreado)).isEqualTo(contenido);
                log.info("Test copiar archivo exitoso: archivo creado en {}", archivoCreado);
            }
        }

        @Test
        @DisplayName("Debe crear el directorio si no existe")
        void debeCrearDirectorioSiNoExiste() throws Exception {
            // Given
            when(archivo.getOriginalFilename()).thenReturn("doc.pdf");
            when(archivo.getInputStream()).thenReturn(new ByteArrayInputStream("pdf content".getBytes()));

            Path directorioEsperado = tempDir.resolve(DIRECTORIO);

            try (MockedStatic<UUID> uuidMock = mockStatic(UUID.class, CALLS_REAL_METHODS)) {
                uuidMock.when(UUID::randomUUID).thenReturn(UUID_FIJO);

                // When
                assertThat(directorioEsperado).doesNotExist();
                uploadFileService.copiar(archivo, DIRECTORIO);

                // Then
                assertThat(directorioEsperado).exists();
                assertThat(directorioEsperado).isDirectory();
                log.info("Test crear directorio: directorio creado en {}", directorioEsperado);
            }
        }

        @Test
        @DisplayName("Debe eliminar espacios en el nombre original del archivo")
        void debeEliminarEspaciosEnNombreOriginal() throws Exception {
            // Given
            String originalFilename = "mi foto de perfil.jpg";

            when(archivo.getOriginalFilename()).thenReturn(originalFilename);
            when(archivo.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));

            try (MockedStatic<UUID> uuidMock = mockStatic(UUID.class, CALLS_REAL_METHODS)) {
                uuidMock.when(UUID::randomUUID).thenReturn(UUID_FIJO);

                // When
                String nombreResultado = uploadFileService.copiar(archivo, DIRECTORIO);

                // Then
                assertThat(nombreResultado).doesNotContain(" ");
                assertThat(nombreResultado).endsWith("mifotodeperfil.jpg");
                log.info("Test eliminar espacios: nombre generado = {}", nombreResultado);
            }
        }

        @Test
        @DisplayName("Debe lanzar IOException cuando el archivo MultipartFile tiene error")
        void debeLanzarIOExceptionCuandoMultipartFileFalla() throws Exception {
            // Given
            when(archivo.getOriginalFilename()).thenReturn("foto.jpg");
            when(archivo.getInputStream()).thenThrow(new IOException("Error de E/S simulado"));

            try (MockedStatic<UUID> uuidMock = mockStatic(UUID.class, CALLS_REAL_METHODS)) {
                uuidMock.when(UUID::randomUUID).thenReturn(UUID_FIJO);

                // When & Then
                IOException exception = org.junit.jupiter.api.Assertions.assertThrows(
                        IOException.class,
                        () -> uploadFileService.copiar(archivo, DIRECTORIO)
                );
                assertThat(exception.getMessage()).isEqualTo("Error de E/S simulado");
                log.info("Test IOException: excepción capturada correctamente");
            }
        }
    }

    // ──────────────────────────────────────────────
    // eliminar()
    // ──────────────────────────────────────────────

    @Nested
    @DisplayName("eliminar()")
    class Eliminar {

        @Test
        @DisplayName("Debe eliminar archivo existente y retornar true")
        void debeEliminarArchivoExistente() throws Exception {
            // Given
            Path directorioPath = tempDir.resolve(DIRECTORIO);
            Files.createDirectories(directorioPath);
            Path archivoPath = directorioPath.resolve(NOMBRE_FOTO);
            Files.writeString(archivoPath, "contenido a eliminar");

            assertThat(archivoPath).exists();

            // When
            boolean resultado = uploadFileService.eliminar(NOMBRE_FOTO, DIRECTORIO);

            // Then
            assertThat(resultado).isTrue();
            assertThat(archivoPath).doesNotExist();
            log.info("Test eliminar archivo existente: eliminado correctamente, resultado = {}", resultado);
        }

        @Test
        @DisplayName("Debe retornar false cuando el archivo no existe")
        void debeRetornarFalseCuandoArchivoNoExiste() throws Exception {
            // When
            boolean resultado = uploadFileService.eliminar("no-existe.jpg", DIRECTORIO);

            // Then
            assertThat(resultado).isFalse();
            log.info("Test eliminar archivo inexistente: retorna false");
        }

        @Test
        @DisplayName("Debe retornar false cuando nombreFoto es null")
        void debeRetornarFalseCuandoNombreEsNull() throws Exception {
            // When
            boolean resultado = uploadFileService.eliminar(null, DIRECTORIO);

            // Then
            assertThat(resultado).isFalse();
            log.info("Test eliminar con nombre null: retorna false sin llamar al sistema de archivos");
        }

        @Test
        @DisplayName("Debe retornar false cuando nombreFoto está vacío")
        void debeRetornarFalseCuandoNombreEsVacio() throws Exception {
            // When
            boolean resultado = uploadFileService.eliminar("", DIRECTORIO);

            // Then
            assertThat(resultado).isFalse();
            log.info("Test eliminar con nombre vacío: retorna false");
        }

        @Test
        @DisplayName("Debe retornar false cuando nombreFoto contiene solo espacios")
        void debeRetornarFalseCuandoNombreEsSoloEspacios() throws Exception {
            // When
            boolean resultado = uploadFileService.eliminar("   ", DIRECTORIO);

            // Then
            assertThat(resultado).isFalse();
            log.info("Test eliminar con nombre solo espacios: retorna false");
        }
    }
}
