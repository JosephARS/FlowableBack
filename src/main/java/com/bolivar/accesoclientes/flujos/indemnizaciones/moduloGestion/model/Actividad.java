package com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.model;

import java.util.Date;

import lombok.Data;

@Data
public class Actividad {
	
	String idActividad;
	String nombre;
	String usuarioAsignado;
	Long duracion;
	Date fechaInicio;
	Date fechaFin;
	String tipoActividad;
	Integer ordenActividad;	
	String processDefinitionId;
	
}
