package com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model;

import java.util.List;

import lombok.ToString;

@ToString
public class Grupo {

	private String agrupador;
	private List<Object> datos;
	
	public Grupo() {
		super();
	}
	
	public Grupo(String agrupador, List<Object> datos) {
		super();
		this.agrupador = agrupador;
		this.datos = datos;
	}



	public String getAgrupador() {
		return agrupador;
	}

	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}

	public List<Object> getDatos() {
		return datos;
	}

	public void setDatos(List<Object> datos) {
		this.datos = datos;
	}

}
