package edu.co.udes.spafeu.web.app.models.enums;


/**
 * <p>
 * Titulo: Proyecto JDS
 * </p>
 * <p>
 * Descripción:enumerador para definición de plantillas de correo a leer
 * </p>
 *
 * @author cristian_misse
 *
 **/
public enum TemplateMailEnum {
	/*plantilladeclaraciontransmisioncreditoswift*/
	PLANTILLA_FORGOT_PASSWORD_USUARIO("plantillaforgotpasswordusuario.html"), 
	PLANTILLA_KYC_APPROBADO("plantillakycaprobado.html"), 
	PLANTILLA_KYC_RECHAZADO("plantillakycrechazado.html"),
	PLANTILLA_ACTIVACION_CUENTA("plantillaactivacioncuenta.html");
	
	private String name;

	private TemplateMailEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
