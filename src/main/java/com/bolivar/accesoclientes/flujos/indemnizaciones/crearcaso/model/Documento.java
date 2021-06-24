package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Documento implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String nombre;
	String urlDescarga;
	
	public Documento() {
		//super();
	}
	
	

}
