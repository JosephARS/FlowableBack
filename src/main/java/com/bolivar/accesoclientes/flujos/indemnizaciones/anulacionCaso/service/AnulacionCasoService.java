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
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Anulacion;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	InfoGeneralProcesoRepository infoProcesoRepository;
	
	@Override
	public ResponseWS anularCaso(String idProceso, Anulacion datosAnulacion) {
		
		System.out.println("ELIMINACION");
		
		ResponseWS oResponseWS = new ResponseWS();
				
	try {
		datosAnulacion.setFechaAnulacion(new Date());
		System.out.println(datosAnulacion.getFechaAnulacion());
		//runtimeService.setVariable(datosAnulacion.getIdProceso() , "Anulacion", datosAnulacion);
		runtimeService.deleteProcessInstance(idProceso, datosAnulacion.getMotivoAnulacion());
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			log.info("procesoIndemnizacion: "+ mapper.writeValueAsString(datosAnulacion));
			infoProcesoRepository.P_ANULAR_CASO(idProceso, mapper.writeValueAsString(datosAnulacion));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}		
		
		oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
		oResponseWS.setMensaje("Anulacion de proceso realizada con exito");
    	log.info("Caso eliminado " + datosAnulacion);
		
	} catch (Exception e) {

		System.err.println(e.getMessage());
		oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
		oResponseWS.setMensaje("Caso no existe " +" | " + "Id proceso no encontrado");
		log.error("Caso no existe " +"|" + e.getMessage() + "|" + e.getClass());
	}

	return oResponseWS;
		
	}
	

}
