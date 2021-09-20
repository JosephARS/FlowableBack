package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class Asegurado {

	@NotNull(message = "El campo tipoDocumento no puede ser nulo.")
	String tipoDocumento;
	@NotNull(message = "El campo numeroDocumento no puede ser nulo.")
	String numeroDocumento;
	@NotNull(message = "El campo nombres no puede ser nulo.")
	String nombres;
	@NotNull(message = "El campo apellidos no puede ser nulo.")
	String apellidos;
	@NotNull(message = "El campo numeroContacto no puede ser nulo.")
	long numeroContacto;
	@NotNull(message = "El campo email no puede ser nulo.")
	String email;
	@Valid
	@NotNull(message = "El campo clv no puede ser nulo.")
	ObjCodigoValor clv;						//Codigo-Valor
	Boolean sarlaftActualizado;
}
