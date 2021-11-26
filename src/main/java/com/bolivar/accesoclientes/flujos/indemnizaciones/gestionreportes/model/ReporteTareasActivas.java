package com.bolivar.accesoclientes.flujos.indemnizaciones.gestionreportes.model;

import java.util.List;

import lombok.Data;

@Data
public class ReporteTareasActivas {

	String tarea;
	List<Detalle> detalle;
	
}
