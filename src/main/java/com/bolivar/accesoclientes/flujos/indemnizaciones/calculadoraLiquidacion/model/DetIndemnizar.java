package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DetIndemnizar {

	String titulo;
	String subTitulo;
	List<SimTypTexto> simTypTexto;
}
