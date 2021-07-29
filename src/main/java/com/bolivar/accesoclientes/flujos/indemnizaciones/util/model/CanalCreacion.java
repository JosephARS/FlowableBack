package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;


import java.io.Serializable;

import lombok.Data;

@Data
public class CanalCreacion implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String codigo;
	String valor;

}
