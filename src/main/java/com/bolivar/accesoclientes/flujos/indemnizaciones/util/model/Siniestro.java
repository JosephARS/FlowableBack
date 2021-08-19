package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class Siniestro {

	Long numeroSiniestro;
	Long valorReserva;
	Long valorPretension;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	Date fechaSiniestro;
	String tipoEvento;
	Date fechaFormalizacion;
	
}
