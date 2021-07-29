package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class Asegurado {

	String tipoDocumento;	
	String numeroDocumento;	
	String nombres;			
	String apellidos;
	Long numeroContacto;
	String email;
	CLV clv;						//Codigo-Valor
	Boolean sarlaftActualizado;
}
