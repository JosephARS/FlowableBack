package com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.DAO;


import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Anulacion;

public interface AnulacionCasoDAO {

	public ResponseWS anularCaso(String idProceso, Anulacion anulacion);
	
	
}
