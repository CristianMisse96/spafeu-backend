package edu.co.udes.spafeu.web.app.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests unitarios para SpafeuException —
 * constructores, getMensaje() y getParametros().</p>
 *
 * @author AGENT-QA-EXPERT-SPAFEU
 */
@Log4j2
@DisplayName("Tests de SpafeuException")
class SpafeuExceptionTest {

    @Nested
    @DisplayName("Constructor SpafeuException(String mensaje)")
    class ConstructorString {

        @Test
        @DisplayName("Debe almacenar el mensaje correctamente")
        void debeAlmacenarMensaje() {
            SpafeuException ex = new SpafeuException("error.usuario.no.encontrado");

            assertThat(ex.getMensaje()).isEqualTo("error.usuario.no.encontrado");
            assertThat(ex.getMessage()).isEqualTo("error.usuario.no.encontrado");
            assertThat(ex.getParametros()).isNull();
            log.info("Constructor(String): mensaje={}", ex.getMensaje());
        }

        @Test
        @DisplayName("Debe tener parametros null")
        void debeTenerParametrosNull() {
            SpafeuException ex = new SpafeuException("error.test");

            assertThat(ex.getParametros()).isNull();
            log.info("Constructor(String): parametros=null");
        }
    }

    @Nested
    @DisplayName("Constructor SpafeuException(String mensaje, List<String> parametros)")
    class ConstructorStringList {

        @Test
        @DisplayName("Debe almacenar mensaje y parámetros")
        void debeAlmacenarMensajeYParametros() {
            List<String> parametros = List.of("param1", "param2");
            SpafeuException ex = new SpafeuException("error.con.parametros", parametros);

            assertThat(ex.getMensaje()).isEqualTo("error.con.parametros");
            assertThat(ex.getMessage()).isEqualTo("error.con.parametros");
            assertThat(ex.getParametros()).containsExactly("param1", "param2");
            log.info("Constructor(String,List): mensaje={}, parametros={}", ex.getMensaje(), ex.getParametros());
        }

        @Test
        @DisplayName("Debe aceptar lista nula de parámetros")
        void debeAceptarParametrosNull() {
            SpafeuException ex = new SpafeuException("error.test", (List<String>) null);

            assertThat(ex.getMensaje()).isEqualTo("error.test");
            assertThat(ex.getParametros()).isNull();
            log.info("Constructor(String,List): parametros null ok");
        }

        @Test
        @DisplayName("Debe aceptar lista vacía de parámetros")
        void debeAceptarParametrosVacios() {
            SpafeuException ex = new SpafeuException("error.test", List.of());

            assertThat(ex.getMensaje()).isEqualTo("error.test");
            assertThat(ex.getParametros()).isEmpty();
            log.info("Constructor(String,List): parametros vacíos ok");
        }
    }

    @Nested
    @DisplayName("Constructor SpafeuException(String mensaje, Throwable cause)")
    class ConstructorStringThrowable {

        @Test
        @DisplayName("Debe almacenar el mensaje y tener la causa")
        void debeAlmacenarMensajeYCausa() {
            RuntimeException cause = new RuntimeException("causa raíz");
            SpafeuException ex = new SpafeuException("error.con.causa", cause);

            assertThat(ex.getMensaje()).isEqualTo("error.con.causa");
            assertThat(ex.getParametros()).isNull();
            assertThat(ex.getCause()).isEqualTo(cause);
            log.info("Constructor(String,Throwable): mensaje={}, cause={}", ex.getMensaje(), ex.getCause().getMessage());
        }

        @Test
        @DisplayName("getMessage() debe retornar la representación de la causa")
        void getMessageDebeRetornarCausa() {
            RuntimeException cause = new RuntimeException("error interno");
            SpafeuException ex = new SpafeuException("error.con.causa", cause);

            assertThat(ex.getMessage()).contains("java.lang.RuntimeException");
            assertThat(ex.getMessage()).contains("error interno");
            log.info("Constructor(String,Throwable): getMessage()='{}'", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Constructor SpafeuException(Exception e)")
    class ConstructorException {

        @Test
        @DisplayName("Debe usar getLocalizedMessage() de la excepción como mensaje")
        void debeUsarLocalizedMessage() {
            Exception original = new Exception("mensaje localizado");
            SpafeuException ex = new SpafeuException(original);

            assertThat(ex.getMensaje()).isEqualTo("mensaje localizado");
            assertThat(ex.getParametros()).isNull();
            assertThat(ex.getCause()).isEqualTo(original);
            log.info("Constructor(Exception): mensaje={}", ex.getMensaje());
        }

        @Test
        @DisplayName("getMessage() debe retornar la representación de la excepción encapsulada")
        void getMessageDebeRetornarExcepcionEncapsulada() {
            Exception original = new Exception("error original");
            SpafeuException ex = new SpafeuException(original);

            assertThat(ex.getMessage()).contains("java.lang.Exception");
            assertThat(ex.getMessage()).contains("error original");
            log.info("Constructor(Exception): getMessage()='{}'", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("getMensaje()")
    class GetMensaje {

        @Test
        @DisplayName("Debe retornar el mensaje cuando no es null ni vacío")
        void debeRetornarMensajeCuandoValido() {
            SpafeuException ex = new SpafeuException("error.valido");

            assertThat(ex.getMensaje()).isEqualTo("error.valido");
            log.info("getMensaje() con mensaje válido: '{}'", ex.getMensaje());
        }

        @Test
        @DisplayName("Debe retornar getMessage() cuando el mensaje es null")
        void debeRetornarGetMessageCuandoMensajeNull() {
            Exception original = new Exception("mensaje desde causa");
            // Usamos constructor(Exception) sin arg de mensaje explícito
            // pero necesitamos que mensaje interno sea null.
            // Crear un caso donde getLocalizedMessage() devuelva null:
            Exception conNullLocalized = new Exception() {
                @Override
                public String getLocalizedMessage() {
                    return null;
                }
            };
            SpafeuException ex = new SpafeuException(conNullLocalized);

            assertThat(ex.getMensaje()).isNotNull();
            assertThat(ex.getMensaje()).contains("SpafeuExceptionTest");
            log.info("getMensaje() con mensaje null: '{}'", ex.getMensaje());
        }

        @Test
        @DisplayName("Debe retornar getMessage() cuando el mensaje está vacío")
        void debeRetornarGetMessageCuandoMensajeVacio() {
            SpafeuException ex = new SpafeuException("");

            assertThat(ex.getMensaje()).isEqualTo("");
            log.info("getMensaje() con mensaje vacío: '{}'", ex.getMensaje());
        }
    }

    @Nested
    @DisplayName("getParametros()")
    class GetParametros {

        @Test
        @DisplayName("Debe retornar la lista de parámetros cuando existe")
        void debeRetornarParametros() {
            List<String> parametros = List.of("user1", "email@test.com");
            SpafeuException ex = new SpafeuException("error.parametros", parametros);

            assertThat(ex.getParametros()).containsExactly("user1", "email@test.com");
            log.info("getParametros() = {}", ex.getParametros());
        }

        @Test
        @DisplayName("Debe retornar null cuando no hay parámetros")
        void debeRetornarNullCuandoNoHay() {
            SpafeuException ex = new SpafeuException("error.simple");

            assertThat(ex.getParametros()).isNull();
            log.info("getParametros() sin parámetros = null");
        }
    }
}
