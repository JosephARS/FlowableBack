package com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Anulacion implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String usuario;
	String motivoAnulacion;
	Date fechaAnulacion;
}

