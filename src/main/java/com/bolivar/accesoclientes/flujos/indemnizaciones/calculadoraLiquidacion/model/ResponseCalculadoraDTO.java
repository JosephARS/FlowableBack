package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResponseCalculadoraDTO {

	Long codRespuesta;
	Long valIndemnizar;
	List<DetIndemnizar> detIndemnizar;
}
