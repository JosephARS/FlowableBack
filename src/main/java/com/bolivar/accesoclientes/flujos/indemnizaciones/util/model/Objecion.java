package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class Objecion {

	String causa;
	String observacion;
	Date fechaObjecion;
	Date fechaNotificacion;
	
}
