package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.util.Date;
import java.util.List;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonIgnoreProperties
public class InfoProceso {

	String idProceso;
	String idConsecutivo;
	ObjCodigoValor estadoCreacion;			//Codigo-Valor
	ObjCodigoValor canalCreacion;	//Codigo-Valor
	String usuarioCreador;
	Date fechaCreacion;
	String estadoSolicitud;
	ObjCodigoValor resultadoScoreRiesgo;
	ObjCodigoValor clasificacionCaso;	//Codigo-Valor
	ObjCodigoValor resultadoMotorDefi;				//Codigo-Valor
	ObjCodigoValor cambioMotorDefi;				//Codigo-Valor
	String motivoCambioMotorDefi;
	String resultadoPruebaEstres;
	List<Documento> documentos; 
}
