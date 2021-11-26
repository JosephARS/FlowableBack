package com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model;

import lombok.ToString;

@ToString
public class ResponseEventosDTO {
	
	Long idSolicitud;
	String aplicacion;
	String codRespuesta;
	String descRespuesta;
	
	public ResponseEventosDTO() {
		super();
	}
	public Long getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(Long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	public String getCodRespuesta() {
		return codRespuesta;
	}
	public void setCodRespuesta(String codRespuesta) {
		this.codRespuesta = codRespuesta;
	}
	public String getDescRespuesta() {
		return descRespuesta;
	}
	public void setDescRespuesta(String descRespuesta) {
		this.descRespuesta = descRespuesta;
	}
	
	

}
