package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.DAO;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;

public interface CalculadoraLiquidacionDAO {

	public ResponseWS mensajePreliquidacion(String idProceso, Boolean clienteAcepta);
	
}
