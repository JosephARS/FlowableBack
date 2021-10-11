package com.bolivar.accesoclientes.flujos.indemnizaciones.actualizarValorReserva.model;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResponseConsultaReserva {

	Long presultado;
	List<?> parrErrores;
	Long prvaTotal;
	List<PcurOut> pcurOut;
}
