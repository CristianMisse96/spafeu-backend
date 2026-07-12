package edu.co.udes.spafeu.web.app.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import lombok.extern.log4j.Log4j2;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para Messages — componente
 * de internacionalización que lee claves del archivo messages.properties
 * a través de MessageSource.</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de Messages")
@Log4j2
class MessagesTest {

    @Mock
    private MessageSource messageSource;

    private Messages messages;

    @BeforeEach
    void setUp() {
        messages = new Messages(messageSource);
    }

    @Nested
    @DisplayName("getKey()")
    class GetKey {

        @Test
        @DisplayName("Debe retornar el valor del mensaje cuando la clave existe en messages.properties")
        void debeRetornarValorCuandoClaveExiste() {
            when(messageSource.getMessage(eq("success"), any(), any(java.util.Locale.class)))
                    .thenReturn("Operación exitosa");

            String resultado = messages.getKey("success");

            assertThat(resultado).isEqualTo("Operación exitosa");
            log.info("getKey('success') = '{}'", resultado);
        }

        @Test
        @DisplayName("Debe retornar mensaje de fallback cuando la clave no existe en messages.properties")
        void debeRetornarFallbackCuandoClaveNoExiste() {
            when(messageSource.getMessage(eq("key.inexistente"), any(), any(java.util.Locale.class)))
                    .thenThrow(new NoSuchMessageException("key.inexistente"));

            String resultado = messages.getKey("key.inexistente");

            assertThat(resultado).isEqualTo("por favor registrar el mensaje en messages.properties key.inexistente");
            log.info("getKey('key.inexistente') = '{}'", resultado);
        }

        @Test
        @DisplayName("Debe retornar mensaje de fallback cuando la clave no existe (con formato correcto)")
        void debeRetornarFallbackConFormatoCorrecto() {
            when(messageSource.getMessage(eq("error.generico"), any(), any(java.util.Locale.class)))
                    .thenThrow(new NoSuchMessageException("error.generico"));

            String resultado = messages.getKey("error.generico");

            assertThat(resultado).startsWith("por favor registrar el mensaje en messages.properties ");
            assertThat(resultado).endsWith("error.generico");
            log.info("getKey('error.generico') = '{}'", resultado);
        }

        @Test
        @DisplayName("Debe lanzar NullPointerException cuando la clave es null")
        void debeLanzarExcepcionCuandoKeyNull() {
            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> messages.getKey(null));

            assertThat(exception.getMessage()).contains("No se encontro registrado en el messages.properties el key=null");
            log.info("getKey(null) lanza NullPointerException");
        }

        @Test
        @DisplayName("Debe lanzar NullPointerException cuando la clave está vacía")
        void debeLanzarExcepcionCuandoKeyVacia() {
            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> messages.getKey(""));

            assertThat(exception.getMessage()).contains("No se encontro registrado en el messages.properties el key=");
            log.info("getKey('') lanza NullPointerException");
        }

        @Test
        @DisplayName("Debe retornar el valor de MessageSource cuando la clave es solo espacios")
        void debeRetornarValorCuandoKeySoloEspacios() {
            when(messageSource.getMessage(eq("   "), any(), any(java.util.Locale.class)))
                    .thenReturn("mensaje-con-espacios");

            String resultado = messages.getKey("   ");

            assertThat(resultado).isEqualTo("mensaje-con-espacios");
            log.info("getKey('   ') = '{}' (ObjectUtils.isEmpty no considera espacios como vacío)", resultado);
        }
    }
}
