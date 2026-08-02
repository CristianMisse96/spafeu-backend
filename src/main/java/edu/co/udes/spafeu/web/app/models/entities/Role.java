package edu.co.udes.spafeu.web.app.models.entities;


import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.co.udes.spafeu.web.app.models.enums.RolEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles", uniqueConstraints = {
		 @UniqueConstraint(columnNames = "ROL", name = "UQ_ROLES_ROL"),
})
@Getter
@Setter
@NoArgsConstructor
public class Role {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_ROLES")
    private Long id;

	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	@Column(name = "ROL", columnDefinition = "role_type",nullable = false)
    private RolEnum rol;
    
    public Role(RolEnum rol) {
		this.rol=rol;
	}
}
