package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VariablesProceso {
		
	
	InfoProceso infoProceso;
	Asegurado asegurado;
	InfoProducto infoProducto;
	//Caso caso;
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
	
}
