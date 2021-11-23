package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestCalculadoraDTO {
	
	long compania;
	long seccion;
	long producto;
	String codUser;
	String numPol;
	String numSecuPol;
	Long numEnd;
	Long codRies;
	String tdocTercero;
	String codAseg;
	String nomAseg;
	String apeAseg;
	String fechaSini;
	String horaSini;
	String descSini;
	Long codCausaSini;
	Long clv;
	Long modeloRiesgo;
	Long valorEncuesta;
	Long codConsSini;
	Long valorEvidencia;
	List<CoberturaCalc> cobertura;
	String validacion;
	ProcesoCalc proceso;

}
