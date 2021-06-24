package com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.DAO;

import com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.model.Anulacion;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;

public interface AnulacionCasoDAO {

	public ResponseWS anularCaso(String idProceso ,Anulacion anulacion);
	
	
}
