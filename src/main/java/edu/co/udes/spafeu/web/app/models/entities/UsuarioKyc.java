package edu.co.udes.spafeu.web.app.models.entities;


import java.time.LocalDate;

import edu.co.udes.spafeu.web.app.models.enums.EstadoKycEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "USER_KYC",
	   uniqueConstraints = {
			   @UniqueConstraint(name = "USUARIO_KYC_USUARIO_ID_UQ", columnNames = "ID_USUARIO")
	        }
)
public class UsuarioKyc {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID_USUARIO_KYC")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;
    
    @Column(name = "FECHA_NACIMIENTO", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "DIRECCION", length = 255, nullable = false)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_KYC", nullable = false, length = 20)
    private EstadoKycEnum estadoKyc;
    
    @Column(name = "NOMBRE", length = 100,  nullable = false)
    private String nombre;
    
    @Column(name = "APELLIDO", length = 100,  nullable = false)
    private String apellido;
}
