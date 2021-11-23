package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CoberturaCalc {

	long codCobSini;
	long valorPretension;
	long modeloRiesgoPretension;
	long clasMotorCaso;
	long motorDefini;
	
}
