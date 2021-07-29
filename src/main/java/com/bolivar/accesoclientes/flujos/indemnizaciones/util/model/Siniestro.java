package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class Siniestro {

	Long numeroSiniestro;
	Long valorReserva;
	Long valorPretension;
	Date fechaSiniestro;
	String tipoEvento;
	Date fechaFormalizacion;
	
}
