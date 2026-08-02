package edu.co.udes.spafeu.web.app.models.entities;

import java.util.ArrayList;
import java.util.List;

import edu.co.udes.spafeu.web.app.models.enums.EstadoKycEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

	 @Entity
	 @Getter
	 @Setter
	 @Table(name = "users", uniqueConstraints = {
	      @UniqueConstraint(columnNames = "USERNAME", name = "UQ_USERS_USERNAME"),
	      @UniqueConstraint(columnNames = "EMAIL", name = "UQ_USERS_EMAIL"),
	 })
	 public class Usuario {

	     @Id
	     @GeneratedValue(strategy = GenerationType.IDENTITY)
	     @Column(name = "ID_USERS", updatable = false, nullable = false)
	     private Long id;

	     @Column(name = "USERNAME", nullable = false, length = 30)
	     private String username;

	     @Column(name = "EMAIL", nullable = false, length = 45)
	     private String email;

	     @Column(name = "PASSWORD", nullable = false, length = 80)
	     private String password;

	     @Column(name = "FOTO", length = 200)
	     private String foto;

	     @Column(name = "ENABLED", nullable = false)
	     private Boolean enabled;

	     @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	     @JoinTable(
	         name = "users_roles",
	         joinColumns = @JoinColumn(name = "ID_USERS"),
	         inverseJoinColumns = @JoinColumn(name = "ID_ROLES"),
	         uniqueConstraints = {@UniqueConstraint(columnNames = {"ID_USERS", "ID_ROLES"})}
	     )
	     private List<Role> roles;

	     @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	     private UserHealthProfile usuarioKyc;

	     public Usuario() {
			 this.roles = new ArrayList<>();
		 }

	     public EstadoKycEnum getEstadoKyc() {
	         return usuarioKyc != null ? usuarioKyc.getEstadoKyc() : EstadoKycEnum.NO_INICIADO;
	     }
	 }
