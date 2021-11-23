package com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class ArchivoDTO {

	String idConsecutivo;
	String idProceso;
	String nombre;
	String archivoBase64;
	
}
