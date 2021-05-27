package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class RespuestaInstanciaProcesoDto {

	String IdProceso;
	boolean Finalizado;
	String IdConsecutivo;

}
