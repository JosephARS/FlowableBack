package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;


import java.util.Date;
import java.util.Map;

import lombok.Data;

@Data
public class Procesos {
	
	public Procesos() {
		// TODO Auto-generated constructor stub
	}
	String IdProceso;
	String IdConsecutivo;
	Date FechaInicio;
	String UsuarioCreador;
	Map<String, Object> VariablesCaso;
	String identificacionCliente;
	String numeroPoliza;
	


}
