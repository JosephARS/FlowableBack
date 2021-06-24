package com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.DAO.AnulacionCasoDAO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.model.Anulacion;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AnulacionCasoService implements AnulacionCasoDAO {
	
	RuntimeService runtimeService;
	
	@Override
	public ResponseWS anularCaso(String idProceso ,Anulacion anulacion) {
		
		System.out.println("ELIMINACION");
		
		ResponseWS oResponseWS = new ResponseWS();
				
	try {
		anulacion.setFechaAnulacion(new Date());
		System.out.println(anulacion.getFechaAnulacion());
		runtimeService.setVariable(idProceso , "Anulacion", anulacion);
		runtimeService.deleteProcessInstance(idProceso, anulacion.getMotivoAnulacion());
		oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
		oResponseWS.setMensaje("Anulacion de caso realizada con exito");
    	log.info("Caso eliminado " + idProceso);
		
	} catch (Exception e) {

		System.err.println(e.getMessage());
		oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
		oResponseWS.setMensaje(e.getMessage() + "" + e.getClass());
		log.error("Caso no existe " +"|" + e.getMessage() + "|" + e.getClass());
	}

	return oResponseWS;
		
	}
	

}
