package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class HistorialAnalisis {

	List<Analisis> analisis;

}
