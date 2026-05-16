package edu.co.udes.spafeu.web.app.service.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * <p>Título: Proyecto SPAFEU</p>
 * <p>Descripción: Tests de contrato para la interfaz AuthService. Verifica que la interfaz define los métodos esperados.</p>
 * @author Sistema SPAFEU
 */
@DisplayName("Tests de Contrato - AuthService")
class AuthServiceTest {

    @Test
    @DisplayName("La interfaz AuthService debe declarar exactamente 3 métodos")
    void interfazDebeDeclararTresMetodos() throws NoSuchMethodException, SecurityException {
        // Arrange & Act
        Method[] methods = AuthService.class.getDeclaredMethods();

        // Assert
        assertEquals(3, methods.length, "AuthService debe declarar exactamente 3 métodos");
    }

    @Test
    @DisplayName("AuthService debe declarar forgotPassword(String)")
    void debeDeclararForgotPassword() throws NoSuchMethodException {
        // Arrange & Act
        Method forgotPasswordMethod = AuthService.class.getMethod("forgotPassword", String.class);

        // Assert
        assertEquals(1, forgotPasswordMethod.getParameterCount(), "forgotPassword debe recibir 1 parámetro");
        assertEquals(String.class, forgotPasswordMethod.getParameterTypes()[0], "El parámetro debe ser String");
    }

    @Test
    @DisplayName("AuthService debe declarar resetPassword(String, String)")
    void debeDeclararResetPassword() throws NoSuchMethodException {
        // Arrange & Act
        Method resetPasswordMethod = AuthService.class.getMethod("resetPassword", String.class, String.class);

        // Assert
        assertEquals(2, resetPasswordMethod.getParameterCount(), "resetPassword debe recibir 2 parámetros");
        assertEquals(String.class, resetPasswordMethod.getParameterTypes()[0], "Primer parámetro debe ser String");
        assertEquals(String.class, resetPasswordMethod.getParameterTypes()[1], "Segundo parámetro debe ser String");
    }

    @Test
    @DisplayName("AuthServiceImpl debe ser una clase concreta (verificación por reflexión)")
    void authServiceImplDebeSerConcreta() throws ClassNotFoundException {
        // Arrange & Act
        Class<?> implClass = Class.forName("edu.co.udes.spafeu.web.app.service.entity.impl.AuthServiceImpl");

        // Assert
        assertFalse(implClass.isInterface(), "AuthServiceImpl debe ser una clase concreta, no interfaz");
        assertFalse(java.lang.reflect.Modifier.isAbstract(implClass.getModifiers()),
                "AuthServiceImpl no debe ser abstracta");
    }
}
