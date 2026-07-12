package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.co.udes.spafeu.web.app.models.dto.MailDTO;
import edu.co.udes.spafeu.web.app.models.enums.TemplateMailEnum;
import edu.co.udes.spafeu.web.app.service.entity.EmailService;
import lombok.extern.log4j.Log4j2;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de CorreoServiceImpl")
@Log4j2
class CorreoServiceImplTest {

    @Mock
    private EmailService emailService;

    private CorreoServiceImpl correoService;

    @BeforeEach
    void setUp() {
        correoService = new CorreoServiceImpl(emailService);
    }

    @Nested
    @DisplayName("generarCorreo()")
    class GenerarCorreo {

        private static final String ASUNTO = "Recuperación de contraseña";
        private static final String CORREO_DESTINO = "usuario@example.com";
        private static final TemplateMailEnum TEMPLATE = TemplateMailEnum.PLANTILLA_FORGOT_PASSWORD_USUARIO;

        @Test
        @DisplayName("Debe enviar correo exitosamente con un solo destinatario")
        void debeEnviarCorreoConUnDestinatario() {
            // Given
            Map<String, Object> parametros = Map.of("nombre", "Juan", "codigo", "12345");

            // When
            correoService.generarCorreo(parametros, CORREO_DESTINO, TEMPLATE, ASUNTO);

            // Then
            verify(emailService).sendMail(
                    argThat((MailDTO mail) -> {
                        assertThat(mail.getPara()).containsExactly(CORREO_DESTINO);
                        assertThat(mail.getCopia()).isNull();
                        assertThat(mail.getAsunto()).isEqualTo(ASUNTO);
                        assertThat(mail.getNombreArchivoAdjunto()).isNull();
                        assertThat(mail.getArchivoAdjunto()).isNull();
                        assertThat(mail.getModel())
                                .containsEntry("nombre", "Juan")
                                .containsEntry("codigo", "12345");
                        assertThat(mail.getArchivoAdjuntoBytes()).isNull();
                        assertThat(mail.getMimeType()).isNull();
                        return true;
                    }),
                    argThat((TemplateMailEnum t) -> t == TEMPLATE)
            );
            log.info("Test generarCorreo con un destinatario: enviado correctamente a {}", CORREO_DESTINO);
        }

        @Test
        @DisplayName("Debe enviar correo con múltiples destinatarios separados por coma")
        void debeEnviarCorreoConMultiplesDestinatarios() {
            // Given
            String correosMultiples = "usuario1@example.com,usuario2@example.com,usuario3@example.com";
            Map<String, Object> parametros = Map.of("nombre", "Carlos");

            // When
            correoService.generarCorreo(parametros, correosMultiples, TEMPLATE, ASUNTO);

            // Then
            verify(emailService).sendMail(
                    argThat((MailDTO mail) -> {
                        assertThat(mail.getPara()).containsExactly(
                                "usuario1@example.com",
                                "usuario2@example.com",
                                "usuario3@example.com"
                        );
                        return true;
                    }),
                    argThat((TemplateMailEnum t) -> t == TEMPLATE)
            );
            log.info("Test generarCorreo con múltiples destinatarios: enviado a 3 correos");
        }

        @Test
        @DisplayName("Debe aplicar trim al string completo antes de split por coma")
        void debeTrimiarStringCompletoAntesDeSplit() {
            // Given
            // NOTA: trim() se aplica al string completo antes de split(",").
            // Los espacios internos alrededor de cada correo NO se eliminan.
            String correosConEspaciosExtremos = "  usuario1@example.com,usuario2@example.com  ";
            Map<String, Object> parametros = Map.of();

            // When
            correoService.generarCorreo(parametros, correosConEspaciosExtremos, TEMPLATE, "Asunto test");

            // Then
            verify(emailService).sendMail(
                    argThat((MailDTO mail) -> {
                        assertThat(mail.getPara()).containsExactly(
                                "usuario1@example.com",
                                "usuario2@example.com"
                        );
                        return true;
                    }),
                    argThat((TemplateMailEnum t) -> t == TEMPLATE)
            );
            log.info("Test trim de string completo: espacios extremos eliminados correctamente");
        }

        @Test
        @DisplayName("Debe enviar correo con parámetros de modelo vacíos")
        void debeEnviarCorreoConModeloVacio() {
            // Given
            Map<String, Object> parametrosVacios = Map.of();

            // When
            correoService.generarCorreo(parametrosVacios, CORREO_DESTINO, TEMPLATE, ASUNTO);

            // Then
            verify(emailService).sendMail(
                    argThat((MailDTO mail) -> {
                        assertThat(mail.getModel()).isEmpty();
                        return true;
                    }),
                    argThat((TemplateMailEnum t) -> t == TEMPLATE)
            );
            log.info("Test generarCorreo con modelo vacío: enviado correctamente");
        }

        @Test
        @DisplayName("Debe enviar correo con asunto personalizado")
        void debeEnviarCorreoConAsuntoPersonalizado() {
            // Given
            String asuntoPersonalizado = "Bienvenido al sistema SPAFEU";
            Map<String, Object> parametros = Map.of("usuario", "Ana");

            // When
            correoService.generarCorreo(parametros, CORREO_DESTINO, TEMPLATE, asuntoPersonalizado);

            // Then
            verify(emailService).sendMail(
                    argThat((MailDTO mail) -> {
                        assertThat(mail.getAsunto()).isEqualTo(asuntoPersonalizado);
                        return true;
                    }),
                    argThat((TemplateMailEnum t) -> t == TEMPLATE)
            );
            log.info("Test asunto personalizado: '{}'", asuntoPersonalizado);
        }

        @Test
        @DisplayName("Debe enviar correo con plantilla KYC_APPROBADO")
        void debeEnviarCorreoConTemplateKycAprobado() {
            // Given
            Map<String, Object> parametros = Map.of("nombre", "Pedro");

            // When
            correoService.generarCorreo(parametros, CORREO_DESTINO,
                    TemplateMailEnum.PLANTILLA_KYC_APPROBADO, "KYC Aprobado");

            // Then
            verify(emailService).sendMail(
                    argThat((MailDTO mail) -> {
                        assertThat(mail.getPara()).containsExactly(CORREO_DESTINO);
                        return true;
                    }),
                    argThat((TemplateMailEnum t) -> t == TemplateMailEnum.PLANTILLA_KYC_APPROBADO)
            );
            log.info("Test con template KYC_APPROBADO: enviado correctamente");
        }

        @Test
        @DisplayName("Debe enviar correo con plantilla KYC_RECHAZADO")
        void debeEnviarCorreoConTemplateKycRechazado() {
            // Given
            Map<String, Object> parametros = Map.of("motivo", "Documentación incompleta");

            // When
            correoService.generarCorreo(parametros, CORREO_DESTINO,
                    TemplateMailEnum.PLANTILLA_KYC_RECHAZADO, "KYC Rechazado");

            // Then
            verify(emailService).sendMail(
                    argThat((MailDTO mail) -> {
                        assertThat(mail.getPara()).containsExactly(CORREO_DESTINO);
                        assertThat(mail.getModel()).containsEntry("motivo", "Documentación incompleta");
                        return true;
                    }),
                    argThat((TemplateMailEnum t) -> t == TemplateMailEnum.PLANTILLA_KYC_RECHAZADO)
            );
            log.info("Test con template KYC_RECHAZADO: enviado correctamente");
        }

        @Test
        @DisplayName("Debe pasar la copia como null en todos los casos")
        void debePasarCopiaComoNull() {
            // Given
            Map<String, Object> parametros = Map.of("nombre", "Luis");

            // When
            correoService.generarCorreo(parametros, CORREO_DESTINO, TEMPLATE, "Asunto");

            // Then
            verify(emailService).sendMail(
                    argThat((MailDTO mail) -> {
                        assertThat(mail.getCopia()).isNull();
                        return true;
                    }),
                    argThat((TemplateMailEnum t) -> t == TEMPLATE)
            );
            log.info("Test copia null: verificado que copia es null");
        }

        @Test
        @DisplayName("Debe pasar archivos adjuntos como null en todos los casos")
        void debePasarAdjuntosComoNull() {
            // Given
            Map<String, Object> parametros = Map.of();

            // When
            correoService.generarCorreo(parametros, CORREO_DESTINO, TEMPLATE, "Asunto");

            // Then
            verify(emailService).sendMail(
                    argThat((MailDTO mail) -> {
                        assertThat(mail.getNombreArchivoAdjunto()).isNull();
                        assertThat(mail.getArchivoAdjunto()).isNull();
                        assertThat(mail.getArchivoAdjuntoBytes()).isNull();
                        assertThat(mail.getMimeType()).isNull();
                        return true;
                    }),
                    argThat((TemplateMailEnum t) -> t == TEMPLATE)
            );
            log.info("Test adjuntos null: verificado que todos los adjuntos son null");
        }
    }
}
