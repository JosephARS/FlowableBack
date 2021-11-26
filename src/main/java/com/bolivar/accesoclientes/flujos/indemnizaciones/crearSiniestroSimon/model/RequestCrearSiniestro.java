package com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestCrearSiniestro {

	long compania;
	long seccion;
	long producto;
	String usuario;
	String numPol;
	String numSecuPol;
	long numEnd;
	long codRies;
	String tdocTercero;
	String codAseg;
	String nomAseg;
	String apeAseg;
	Date fechaSini;
	String horaSini;
	String descSini;
	long codCausaSini;
	long clv;
	long modeloRiesgo;
	long valorEncuesta;
	long motorCaso;
	long motorDefinicion;
	String motivoCambio;
	List<ReqConseSini> consecuencia;
	String validacion;
	ReqProcesoSini proceso;
}
