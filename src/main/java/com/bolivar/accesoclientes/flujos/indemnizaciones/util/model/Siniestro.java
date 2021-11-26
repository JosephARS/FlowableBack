package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class Siniestro {

	Long numeroSiniestro = (long) 0;
	Long valorReserva = (long) 0;
	@NotNull
	Long valorPretension;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	Date fechaSiniestro;
	String horaSiniestro;
	String tipoEvento;
	Date fechaFormalizacion;
	
}
