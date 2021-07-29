package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestCalculadoraDTO {
	
	long codCia;
	long codSecc;
	long codRamo;
	long numPol1;
	long codRies;
	Date fechaSini;
	long codCausa;
	long codCons;
	long codCob;
	long valPretension;
	String validacion;
	ProcesoCalc proceso;

}
