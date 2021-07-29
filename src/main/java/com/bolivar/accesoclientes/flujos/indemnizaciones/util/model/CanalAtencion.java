package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class CanalAtencion {

	String lineaAtencion;
	String responsable;
	String cambioResponsable;
	String casoStellent;	
	
}
