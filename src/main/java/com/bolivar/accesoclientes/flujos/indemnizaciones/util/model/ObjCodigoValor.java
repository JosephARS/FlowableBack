package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ObjCodigoValor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	String codigo;
	String valor;
}
