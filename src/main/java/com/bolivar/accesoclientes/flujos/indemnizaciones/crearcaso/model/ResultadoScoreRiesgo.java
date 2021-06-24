package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResultadoScoreRiesgo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String codigo;
	String valor;

}
