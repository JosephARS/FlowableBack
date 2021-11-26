package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class Pago {

	String beneficiario;
	String concepto;
	String localidadGiro;
	Long facturaNumero;
	Long valorPago;
	String controlTecnico;
	String autorizante;
	Long numeroLiquidacion;
	String resultadoDispersion;
	Date fechaPago;
	Date fechaActualizaEstFinan;
	String respuestaClientePreliquidacion;
	
}
