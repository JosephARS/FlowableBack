package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.DAO;

import java.util.List;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.VariablesProceso;

public interface CrearCasoDAO {
	

	
	public List<Object> trazaHistoricoProceso(String processId);

	public ResponseWS registrarNuevoCaso(VariablesProceso procesoIndemnizacion);

}


