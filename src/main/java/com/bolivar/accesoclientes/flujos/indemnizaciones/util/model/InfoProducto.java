package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class InfoProducto {

	@Valid
	@NotNull
	ObjCodigoValor compania;
	@Valid
	@NotNull
	ObjCodigoValor ramo;		//Codigo-Valor
	@Valid
	@NotNull
	ObjCodigoValor producto;				//Codigo-Valor
	@NotBlank
	Long numeroPoliza;
	@NotBlank
	Long numSecuPoliza;
	@NotBlank
	Long numEndoso;
	@Valid
	@NotNull
	ObjCodigoValor causa;						//ArrayList[Codigo-Valor]
	@Valid
	@NotNull
	List<Cobertura> cobertura;				//ArrayList[Codigo-Valor]
	@Valid
	@NotNull
	ObjCodigoValor riesgo;
	
}
