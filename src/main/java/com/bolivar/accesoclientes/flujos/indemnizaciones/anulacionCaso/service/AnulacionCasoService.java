package com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.DAO.AnulacionCasoDAO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Anulacion;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.UsuariosRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
	TaskService taskService;
	InfoGeneralProcesoRepository infoProcesoRepository;
	UsuariosRepository usuariosRepository;
//	IdentityService identityService;
//	IdentityLink identityLink;
//	ProcessEngine processEngine;
	
	@Override
	public ResponseWS anularCaso(String idProceso, Anulacion datosAnulacion) {
		
		System.out.println("ELIMINACION");
		
		ResponseWS oResponseWS = new ResponseWS();
				
	try {
		datosAnulacion.setFechaAnulacion(new Date());

		List<Task> tareasActivas = taskService.createTaskQuery().processInstanceId(idProceso).includeIdentityLinks().list();
		
		for (Task tarea : tareasActivas) {
			
			System.out.println("Asignado: " + tarea.getAssignee());
			usuariosRepository.P_TAREA_CERRADA(tarea.getAssignee() == null ? "":tarea.getAssignee(), "Anulada");
		}
		
//		List<ActivityInstance> tareasActivas2 = runtimeService.createActivityInstanceQuery().processInstanceId(idProceso).unfinished().activityType("userTask").list();
//		for (ActivityInstance tarea : tareasActivas2) {
//			
//			System.out.println("Asignado2: " + tarea.toString());
//			System.out.println("Asignado2: " + tarea.getAssignee());
//			System.out.println("Asignado2: " + taskService.getIdentityLinksForTask(tarea.getTaskId()));
//			
//		}
		
		runtimeService.deleteProcessInstance(idProceso, datosAnulacion.getMotivoAnulacion());
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
