package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonIgnoreProperties
public class InfoProceso {

	String idProceso;
	String idConsecutivo;
	@Valid
	@NotNull(message = "El campo estadoCreacion no puede ser nulo.")
	ObjCodigoValor estadoCreacion;			//Codigo-Valor
	@Valid
	@NotNull(message = "El campo canalCreacion no puede ser nulo.")
	ObjCodigoValor canalCreacion;	//Codigo-Valor
	String usuarioCreador;
	Date fechaCreacion;
	String estadoSolicitud;
	String estadoFinal;
	@Valid
	@NotNull(message = "El campo resultadoScoreRiesgo no puede ser nulo.")
	ObjCodigoValor resultadoScoreRiesgo;
	@Valid
	@NotNull(message = "El campo clasificacionCaso no puede ser nulo.")
	ObjCodigoValor clasificacionCaso;	//Codigo-Valor
	ObjCodigoValor resultadoMotorDefi;				//Codigo-Valor
	ObjCodigoValor cambioMotorDef;				//Codigo-Valor
	String motivoCambioMotorDef;
	String resultadoPruebaEstres;
	List<Documento> documentos;	  
	String resultadoEvidencia;
	Date fechaDesistimiento ;
	String varProducto;
}
