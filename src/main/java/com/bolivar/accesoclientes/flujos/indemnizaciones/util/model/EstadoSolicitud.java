package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import lombok.Data;

@Data
public class EstadoSolicitud {

	public static final String AVISADO = "Avisado";
	public static final String FORMALIZADO = "Formalizado";
	public static final String PENDIENTE_FORMALIZAR = "Pendiente formalizar";
	public static final String PENDIENTE_AUTORIZAR = "Pendiente autorizar";
	public static final String OBJETADO = "Objetado";
	public static final String PAGO = "Pago";
	public static final String OTRA_LINEA = "Otra linea de atencion";
	public static final String TERMINADO = "Terminado";
	public static final String ANULADO = "Anulado";
	
}
