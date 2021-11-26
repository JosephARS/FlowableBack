package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ConsolidadoResp {
	Long vrIndemnizable;
	Long vrDeducible;
	Long vrAindemnizar;
	Long vrCartera;
	Long totalaIndemnizar;
}
