package com.bolivar.accesoclientes.flujos.indemnizaciones.asignarUsuarioTarea.handler;


import java.util.Arrays;
import java.util.List;
import java.util.Random;


import org.flowable.engine.delegate.TaskListener;
import org.flowable.identitylink.api.IdentityLinkType;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.EstadoSolicitud;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.UsuariosRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GestionadorAsignarUsuario implements TaskListener {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	UsuariosRepository usuariosRepository;
	
	@Autowired
	InfoGeneralProcesoRepository infoProcesoRepository;
	
	  public void notify(DelegateTask delegateTask) {
		
		String idProceso = delegateTask.getProcessInstanceId();
		String idDefinicionTarea =delegateTask.getTaskDefinitionKey();
		String usuarioAsignado = usuariosRepository.P_OBTENER_USUARIO(idProceso, idDefinicionTarea);
		Integer respuesta = 0;
		Integer respuesta2 = 0;
		System.out.println("proceso:" + idProceso);
		log.info("Usuario asignado exitosamente a la tarea: " + usuarioAsignado);
		System.out.println(idDefinicionTarea);


		
		delegateTask.setDueDate(null);
		delegateTask.setOwner(usuarioAsignado);
		delegateTask.setAssignee(usuarioAsignado);
		delegateTask.addUserIdentityLink(usuarioAsignado, IdentityLinkType.ASSIGNEE);
		delegateTask.addUserIdentityLink(usuarioAsignado, IdentityLinkType.OWNER);
		delegateTask.addCandidateUser(usuarioAsignado);
		
		respuesta = usuariosRepository.P_ACTUALIZAR_USUARIO_ASIGNADO(idProceso, usuarioAsignado, "Responsable");
		
		switch (idDefinicionTarea) {
		case "userTask_ConceptoAnalisisCaso":
			respuesta2 = infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.FORMALIZADO);
			break;
		case "userTask_ActualizacionValorReserva":
			respuesta2 = infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.AVISADO);
			break;
		case "userTask_AnalisisCasoObjetado":
			respuesta2 = infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.OBJETADO);
			break;
		case "userTask_ValidacionLiquidacion":
			respuesta2 = infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.FORMALIZADO);
			break;
		case "userTask_ExplicacionCasoObjetado":
			respuesta2 = infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.OBJETADO);
			break;
		case "userTask_LevantamientoControlSimon":
			respuesta2 = infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.PENDIENTE_AUTORIZAR);
			break;
		default:
			break;
		}
		
		
		if (respuesta == 0 && respuesta2 == 0) {
			System.out.println("Owner:" + delegateTask.getOwner());
			System.out.println("Asignado:" + delegateTask.getAssignee());
			System.out.println("Candidatos" + delegateTask.getCandidates());
			System.out.println("/n"
			+ "/n");
			log.info(usuarioAsignado + "Usuario asignado exitosamente a la tarea: " + idDefinicionTarea);
		}else {
			log.error("Error actualizando usuario asignado a la tarea: " + idDefinicionTarea);
		}
				
		
		}
	  
	    public String generarUsuario() {
	        List<String> listaUsuarios = Arrays.asList("Analista1");//("Analista1","Analista2","Analista3");
	        Random random = new Random();
	        String randomElement = listaUsuarios.get(random.nextInt(listaUsuarios.size()));
			return randomElement;
	    }	    
	    

}
