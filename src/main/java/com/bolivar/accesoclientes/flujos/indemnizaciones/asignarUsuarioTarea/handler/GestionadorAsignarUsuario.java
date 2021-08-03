package com.bolivar.accesoclientes.flujos.indemnizaciones.asignarUsuarioTarea.handler;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.identitylink.api.IdentityLinkType;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.UsuariosRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GestionadorAsignarUsuario implements TaskListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	UsuariosRepository usuariosRepository;
	
	TaskService taskService;
	
	  public void notify(DelegateTask delegateTask) {
		  
		String idProceso = delegateTask.getProcessInstanceId();
		String idDefinicionTarea =delegateTask.getTaskDefinitionKey();
		String usuarioAsignado = usuariosRepository.P_OBTENER_USUARIO(idProceso, idDefinicionTarea);
		System.out.println("proceso:" + idProceso);
		log.info("Usuario asignado exitosamente a la tarea: " + usuarioAsignado);
		System.out.println(delegateTask.getTaskDefinitionKey());

		//String usuarioAsignado = generarUsuario();
		  
		delegateTask.setOwner(usuarioAsignado);
		delegateTask.setAssignee(usuarioAsignado);
		delegateTask.addUserIdentityLink(usuarioAsignado, IdentityLinkType.ASSIGNEE);
		delegateTask.addUserIdentityLink(usuarioAsignado, IdentityLinkType.OWNER);
		delegateTask.addCandidateUser(usuarioAsignado);
		
		int respuesta = usuariosRepository.P_ACTUALIZAR_USUARIO_ASIGNADO(idProceso, usuarioAsignado);
		
		if (respuesta == 0) {
			System.out.println("Owner:" + delegateTask.getOwner());
			System.out.println("Asignado:" + delegateTask.getAssignee());
			System.out.println("Candidatos" + delegateTask.getCandidates());
			System.out.println("/n"
			+ "/n");
			log.info("Usuario asignado exitosamente a la tarea: " + idDefinicionTarea);
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
