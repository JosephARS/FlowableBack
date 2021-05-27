package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.DAO;

import java.util.List;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.NuevoCaso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.RespuestaInstanciaProcesoDto;

public interface CrearCasoDAO {
	
	public RespuestaInstanciaProcesoDto registrarNuevoCaso(NuevoCaso nuevoCaso);
	
	public List<Object> trazaHistoricoProceso(String processId);

}


