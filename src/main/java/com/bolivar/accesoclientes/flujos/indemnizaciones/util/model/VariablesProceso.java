package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties
public class VariablesProceso {
		
	@Valid
	InfoProceso infoProceso;
	@Valid
	Asegurado asegurado;
	InfoProducto infoProducto;
	Siniestro siniestro;
	
		@JsonInclude(JsonInclude.Include.NON_NULL)
	CanalAtencion canalAtencion;
	
		@JsonInclude(JsonInclude.Include.NON_NULL)
	Pago pago;
	
		@JsonInclude(JsonInclude.Include.NON_NULL)
	Anulacion anulacion;
	
		@JsonInclude(JsonInclude.Include.NON_NULL)
	Objecion objecion;
	
		@JsonInclude(JsonInclude.Include.NON_NULL)
	Ajustador ajustador;
	
		@JsonInclude(JsonInclude.Include.NON_NULL)
	List<HistorialAnalisis> historialAnalisis;
}
