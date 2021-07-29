package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class InfoProducto {

	
	ObjCodigoValor compania;
	ObjCodigoValor ramo;		//Codigo-Valor
	ObjCodigoValor producto;				//Codigo-Valor
	Long numeroPoliza;	
	List<ObjCodigoValor> causa;						//ArrayList[Codigo-Valor]
	List<ObjCodigoValor> consecuencia;		//ArrayList[Codigo-Valor]
	List<ObjCodigoValor> cobertura;				//ArrayList[Codigo-Valor]
	ObjCodigoValor riesgo;
	
}
