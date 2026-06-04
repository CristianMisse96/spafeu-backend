package edu.co.udes.spafeu.web.app.service.entity.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import edu.co.udes.spafeu.web.app.models.dto.MailDTO;
import edu.co.udes.spafeu.web.app.models.enums.TemplateMailEnum;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de EmailServiceImpl")
class EmailServiceImplTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "from", "test@spafeu.com");
    }

    @Nested
    @DisplayName("sendMail()")
    class SendMailTests {

        @Test
        @DisplayName("Debe enviar correo exitosamente sin CC y sin adjuntos")
        void sendMail_exitoso_basico() throws MessagingException {
            // Given
            MailDTO mail = MailDTO.builder()
                    .para(new String[]{"dest@example.com"})
                    .asunto("Asunto Básico")
                    .model(Map.of("name", "Test"))
                    .build();

            when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
            when(templateEngine.process(eq("email/plantillaforgotpasswordusuario.html"), any(Context.class)))
                    .thenReturn("<html>Basic</html>");

            // When
            try (MockedConstruction<MimeMessageHelper> mocked = mockConstruction(MimeMessageHelper.class)) {
                emailService.sendMail(mail, TemplateMailEnum.PLANTILLA_FORGOT_PASSWORD_USUARIO);

                // Then
                MimeMessageHelper helper = mocked.constructed().get(0);
                verify(helper).setFrom("test@spafeu.com");
                verify(helper).setTo(mail.getPara());
                verify(helper).setSubject("Asunto Básico");
                verify(helper).setText("<html>Basic</html>", true);
                verify(emailSender).send(mimeMessage);
                verify(helper, never()).setCc(any(String[].class));
                verify(helper, never()).addAttachment(any(String.class), ArgumentMatchers.<File>any());
                verify(helper, never()).addAttachment(any(String.class), ArgumentMatchers.<ByteArrayDataSource>any());
            }
        }

        @Test
        @DisplayName("Debe enviar correo exitosamente con CC")
        void sendMail_exitoso_conCopia() throws MessagingException {
            // Given
            MailDTO mail = MailDTO.builder()
                    .para(new String[]{"dest@example.com"})
                    .copia(new String[]{"cc@example.com"})
                    .asunto("Asunto CC")
                    .model(Map.of())
                    .build();

            when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
            when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("<html>CC</html>");

            // When
            try (MockedConstruction<MimeMessageHelper> mocked = mockConstruction(MimeMessageHelper.class)) {
                emailService.sendMail(mail, TemplateMailEnum.PLANTILLA_KYC_APPROBADO);

                // Then
                MimeMessageHelper helper = mocked.constructed().get(0);
                verify(helper).setCc(mail.getCopia());
                verify(helper, never()).addAttachment(any(String.class), ArgumentMatchers.<File>any());
                verify(helper, never()).addAttachment(any(String.class), ArgumentMatchers.<ByteArrayDataSource>any());
            }
        }

        @Test
        @DisplayName("Debe enviar correo exitosamente con adjunto de bytes")
        void sendMail_exitoso_conAdjuntoBytes() throws MessagingException {
            // Given
            MailDTO mail = MailDTO.builder()
                    .para(new String[]{"dest@example.com"})
                    .asunto("Asunto Adjunto Bytes")
                    .nombreArchivoAdjunto("doc.pdf")
                    .archivoAdjuntoBytes(new byte[]{1, 2, 3})
                    .mimeType("application/pdf")
                    .model(Map.of())
                    .build();

            when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
            when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("<html>Bytes</html>");

            // When
            try (MockedConstruction<MimeMessageHelper> mocked = mockConstruction(MimeMessageHelper.class)) {
                emailService.sendMail(mail, TemplateMailEnum.PLANTILLA_KYC_RECHAZADO);

                // Then
                MimeMessageHelper helper = mocked.constructed().get(0);
                verify(helper).addAttachment(eq("doc.pdf"), any(ByteArrayDataSource.class));
                verify(helper, never()).addAttachment(eq("doc.pdf"), ArgumentMatchers.<File>any());
            }
        }

        @Test
        @DisplayName("Debe enviar correo exitosamente con adjunto File")
        void sendMail_exitoso_conAdjuntoFile() throws MessagingException {
            // Given
            File fileSpy = spy(new File("dummy.txt"));
            doReturn(true).when(fileSpy).exists();

            MailDTO mail = MailDTO.builder()
                    .para(new String[]{"dest@example.com"})
                    .asunto("Asunto Adjunto File")
                    .nombreArchivoAdjunto("file.txt")
                    .archivoAdjunto(fileSpy)
                    .model(Map.of())
                    .build();

            when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
            when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("<html>File</html>");

            // When
            try (MockedConstruction<MimeMessageHelper> mocked = mockConstruction(MimeMessageHelper.class)) {
                emailService.sendMail(mail, TemplateMailEnum.PLANTILLA_FORGOT_PASSWORD_USUARIO);

                // Then
                MimeMessageHelper helper = mocked.constructed().get(0);
                verify(helper).addAttachment(eq("file.txt"), eq(fileSpy));
            }
        }

        @Test
        @DisplayName("Debe enviar correo exitosamente con ambos adjuntos")
        void sendMail_exitoso_conAmbosAdjuntos() throws MessagingException {
            // Given
            File fileSpy = spy(new File("dummy.txt"));
            doReturn(true).when(fileSpy).exists();

            MailDTO mail = MailDTO.builder()
                    .para(new String[]{"dest@example.com"})
                    .asunto("Asunto Ambos")
                    .nombreArchivoAdjunto("both.pdf")
                    .archivoAdjunto(fileSpy)
                    .archivoAdjuntoBytes(new byte[]{4, 5, 6})
                    .mimeType("application/pdf")
                    .model(Map.of())
                    .build();

            when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
            when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("<html>Ambos</html>");

            // When
            try (MockedConstruction<MimeMessageHelper> mocked = mockConstruction(MimeMessageHelper.class)) {
                emailService.sendMail(mail, TemplateMailEnum.PLANTILLA_KYC_APPROBADO);

                // Then
                MimeMessageHelper helper = mocked.constructed().get(0);
                verify(helper).addAttachment(eq("both.pdf"), any(ByteArrayDataSource.class));
                verify(helper).addAttachment(eq("both.pdf"), eq(fileSpy));
            }
        }

        @Test
        @DisplayName("Debe capturar MessagingException y loguear error")
        void sendMail_errorMessagingException() throws MessagingException {
            // Given
            MailDTO mail = MailDTO.builder()
                    .para(new String[]{"dest@example.com"})
                    .asunto("Asunto Error")
                    .model(Map.of())
                    .build();

            when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

            // When & Then
            try (MockedConstruction<MimeMessageHelper> mocked = mockConstruction(MimeMessageHelper.class, (helper, context) -> {
                doThrow(new MessagingException("mail error", new IllegalStateException("cause msg"))).when(helper).setFrom(anyString());
            })) {
                assertDoesNotThrow(() -> emailService.sendMail(mail, TemplateMailEnum.PLANTILLA_KYC_RECHAZADO));
                verify(emailSender, never()).send(any(MimeMessage.class));
            }
        }

        @Test
        @DisplayName("Debe capturar excepción genérica y loguear error")
        void sendMail_errorGenerico() {
            // Given
            MailDTO mail = MailDTO.builder()
                    .para(new String[]{"dest@example.com"})
                    .asunto("Asunto Error Genérico")
                    .model(Map.of())
                    .build();

            when(emailSender.createMimeMessage()).thenThrow(new RuntimeException("generic error", new IllegalStateException("cause msg")));

            // When & Then
            assertDoesNotThrow(() -> emailService.sendMail(mail, TemplateMailEnum.PLANTILLA_FORGOT_PASSWORD_USUARIO));
            verify(emailSender, never()).send(any(MimeMessage.class));
        }

        @Test
        @DisplayName("buildGenericMessage debe usar templateEngine con path correcto")
        void buildGenericMessage_debeUsarTemplateEngine() throws MessagingException {
            // Given
            MailDTO mail = MailDTO.builder()
                    .para(new String[]{"dest@example.com"})
                    .asunto("Asunto Template")
                    .model(Map.of("code", "12345"))
                    .build();

            when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

            // When
            try (MockedConstruction<MimeMessageHelper> mocked = mockConstruction(MimeMessageHelper.class)) {
                emailService.sendMail(mail, TemplateMailEnum.PLANTILLA_KYC_RECHAZADO);

                // Then
                verify(templateEngine).process(
                        eq("email/plantillakycrechazado.html"),
                        argThat((Context ctx) -> "12345".equals(ctx.getVariable("code")))
                );
            }
        }
    }
}
