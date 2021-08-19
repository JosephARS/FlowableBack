package com.bolivar.accesoclientes.flujos.indemnizaciones.asignarUsuarioTarea.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
@Table(name="usuario")
public class Usuario implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	Long idUsuario;
	String identificacion;
	String loginUsuario;
	int activo;
	int tareasActivas;
	int tareasCerradas;
}
