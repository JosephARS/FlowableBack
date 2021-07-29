package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProcesoCalc {

	String modulo;
	long proceso;
	String subProceso;
	long codCia;	
	
}
