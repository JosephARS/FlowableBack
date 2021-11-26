package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResponseErrorDTO {

	String errorID;
	String errorMessage;
	List<String> errors;
	String path;
	
}
