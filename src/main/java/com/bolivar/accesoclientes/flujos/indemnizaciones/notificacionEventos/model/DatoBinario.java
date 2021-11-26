package com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model;

import lombok.ToString;

@ToString
public class DatoBinario {

	private String codDato;
	private String valDato;
	private String bindato;
	
	public DatoBinario(String codDato, String valDato) {
		super();
		this.codDato = codDato;
		this.valDato = valDato;
	}

	public DatoBinario(String codDato, String valDato, String bindato) {
		super();
		this.codDato = codDato;
		this.valDato = valDato;
		this.bindato = bindato;
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


	public void setBindato(String bindato) {
		this.bindato = bindato;
	}

	public String getBindato() {
		return bindato;
	}
	
	
}
