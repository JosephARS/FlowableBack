package com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model;

import java.util.List;

import lombok.ToString;

@ToString
public class RequestEventosDTO {
	
	private String aplicacion;
	private String evento;
	private List<Grupo>	grupo;
	
	public RequestEventosDTO() {
		super();
	}
	
	public String getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	public String getEvento() {
		return evento;
	}
	public void setEvento(String evento) {
		this.evento = evento;
	}
	public List<Grupo> getGrupo() {
		return grupo;
	}
	public void setGrupo(List<Grupo> grupo) {
		this.grupo = grupo;
	}
	
	

}
