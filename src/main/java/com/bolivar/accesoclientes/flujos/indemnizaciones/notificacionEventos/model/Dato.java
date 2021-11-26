package com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model;

import lombok.ToString;

@ToString
public class Dato {

	private String codDato;
	private String valDato;
	
	public Dato(String codDato, String valDato) {
		super();
		this.codDato = codDato;
		this.valDato = valDato;
	}

	public String getCodDato() {
		return codDato;
	}

	public void setCodDato(String codDato) {
		this.codDato = codDato;
	}

	public String getValDato() {
		return valDato;
	}

	public void setValDato(String valDato) {
		this.valDato = valDato;
	}

	
}
