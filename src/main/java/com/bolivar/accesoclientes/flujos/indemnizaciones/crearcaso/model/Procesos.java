package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;

import java.util.Date;


public class Procesos {
	
	public Procesos() {
		// TODO Auto-generated constructor stub
	}
	String IdProceso;
	String IdCasoPropio;
	Date FechaInicio;
	String UsuarioCreador;
	
	public String getIdProceso() {
		return IdProceso;
	}
	public void setIdProceso(String idProceso) {
		IdProceso = idProceso;
	}
	public String getIdCasoPropio() {
		return IdCasoPropio;
	}
	public void setIdCasoPropio(String idCasoPropio) {
		IdCasoPropio = idCasoPropio;
	}
	public Date getFechaInicio() {
		return FechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		FechaInicio = fechaInicio;
	}
	public String getUsuarioCreador() {
		return UsuarioCreador;
	}
	public void setUsuarioCreador(String usuarioCreador) {
		UsuarioCreador = usuarioCreador;
	}

}
