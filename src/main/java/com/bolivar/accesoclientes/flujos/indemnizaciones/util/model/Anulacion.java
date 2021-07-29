package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class Anulacion {

	String usuario;
	String motivoAnulacion;
	Date fechaAnulacion;
	
}
