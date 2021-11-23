package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;


import lombok.Data;

@Data
public class Consecutivos {

	String idConsecutivo;
	String idFlowable;
	
	public Consecutivos(String idConsecutivo, String idFlowable) {
		this.idConsecutivo = idConsecutivo;
		this.idFlowable = idFlowable;
	}
	
	public Consecutivos() {

	}
	
}
