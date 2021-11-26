package com.bolivar.accesoclientes.flujos.indemnizaciones.respuestaPreliquidacionCliente.DAO;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;

public interface RespuestaPreliquidacionClienteDAO {
	
	public ResponseWS mensajePreliquidacion(String idProceso, Boolean clienteAcepta);

}
