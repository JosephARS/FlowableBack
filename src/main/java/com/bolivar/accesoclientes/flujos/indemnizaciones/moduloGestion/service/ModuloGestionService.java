package com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.ActivityInstanceQuery;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.eventsubscription.api.EventSubscription;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.bolivar.accesoclientes.flujos.indemnizaciones.actualizarValorReserva.service.ActualizarValorReservaService;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.EstadoCaso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.Procesos;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;
import com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.DAO.ModuloGestionDAO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.model.Actividad;
import com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.model.Identificacion;
import com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.model.TareasCantidad;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Anulacion;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.CanalCreacion;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.EstadoCreacion;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.InfoGeneralProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.ObjCodigoValor;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.TareaDefinicion;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.VariablesProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ModuloGestionService implements ModuloGestionDAO {
	
	TaskService taskService;
	RuntimeService runtimeService;
	ProcessEngine processEngine;
	RepositoryService repositoryService;
	InfoGeneralProcesoRepository infoProcesoRepository;
	ActualizarValorReservaService actualizarValorReservaService;
	

	@Override
	public ResponseWS obtenerTareasUsuario(String nombreUsuario, String nombreTarea, Integer cantidadItems, Integer primerItem) {

		ResponseWS oResponseWS = new ResponseWS();
		
		List<Object> oTareaList = new ArrayList<Object>();
		
		try {
	        List<Task> tareas = taskService.createTaskQuery()
	                .taskAssignee(nombreUsuario)
	        		.taskName(nombreTarea)
	                .includeProcessVariables()
	                .orderByTaskCreateTime()
	                .desc()
	                //.list();
	                .listPage(primerItem, cantidadItems);
	        
	        long totalTareas = taskService.createTaskQuery()
	                .taskAssignee(nombreUsuario)
	        		.taskName(nombreTarea)
	        		.count();
	        
	        
	        
	        System.out.println(tareas);
	        
	        for (Task tarea : tareas) {		

	            Map<String, Object> map = new LinkedHashMap<>();
	            map.put("idConsecutivo", tarea.getProcessVariables().get("idConsecutivo"));
	            map.put("idProceso", tarea.getProcessInstanceId());
	            map.put("idTarea", tarea.getId());
	            map.put("idTareaDefinicion", tarea.getTaskDefinitionKey());
	            map.put("nombreTarea", tarea.getName());
	            map.put("fechaCreacion", tarea.getCreateTime());
	            map.put("fechaSolucion", tarea.getCreateTime());
	            
	            oTareaList.add(map);
	        
	        }
	        
	        oResponseWS.setTotalItems(totalTareas);
	        oResponseWS.setListaResultado(oTareaList);
	    	oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
	        
		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo tareas " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
			log.error("Error obteniendo tareas " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
			
		}        
        
		return oResponseWS;
		
	}
	
	@Override
	public ResponseWS cantidadTareasUsuario(String nombreUsuario) {
		
		ResponseWS oResponseWS = new ResponseWS();
		
		try {
			List<Task> tareas = taskService.createTaskQuery()
	                .taskAssignee(nombreUsuario)
	                .list();

			System.out.println("nombreUsuario:"+nombreUsuario);
	        System.out.println(tareas);
	        
	        List<String> conteoTareas = new ArrayList<String>();
	        
//	        for (Task tarea : tareas) {		
//
//	        	conteoTareas.add(tarea.getName());
//	        	conteoTareas.add("Tarea 2");
//	        
//	        }
	        
	        tareas.forEach(tarea -> {
	        	conteoTareas.add(tarea.getName());
	        	conteoTareas.add("Tarea 2");
	        });
	        
	        Map<Object, Long> couterMap = conteoTareas.stream().collect(Collectors.groupingBy(e -> e.toString(),Collectors.counting())); //Agrupa y cuentas la cantidad de tareas
	        System.out.println("CounterMap"+couterMap);
	        
	        Map<String, String> mapaObjeto = new HashMap<String, String>();
	        List<Object> listaTareasCantidad = new ArrayList<Object>();
	        
	        for (Entry<Object, Long> counter : couterMap.entrySet()) {
	        	
	        	TareasCantidad oTareasCantidad = new TareasCantidad();
	        	System.out.println("Pruebaaaaaaaaaaaa " + counter.getKey() + counter.getValue());
	        	mapaObjeto.put("codigo", counter.getKey().toString());
	        	mapaObjeto.put("valor", counter.getValue().toString());
	        	
	        	oTareasCantidad.setCodigo(counter.getKey().toString());
	        	oTareasCantidad.setValor(counter.getValue());
	        	listaTareasCantidad.add(oTareasCantidad);
	        
	        }
	        
	        oResponseWS.setListaResultado(listaTareasCantidad);
	    	oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
	        
		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo cantidad tareas " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
			log.error("Error obteniendo cantidad tareas " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
		}
        
        
		return oResponseWS;
	
	}

	
	
	@Override
	public ResponseWS obtenerProcesosIdentificacion(String identificacion, Integer cantidadItems, Integer primerItem) {
		
		ResponseWS oResponseWS = new ResponseWS();
		
		List<Object> oProcesoList = new ArrayList<Object>();
		
		HistoryService historyService = processEngine.getHistoryService();		
		
		try {
			List<ProcessInstance> procesos = runtimeService.createProcessInstanceQuery() 	// Se valida los procesos creados para dicho numero de identificacion de asegurado.
					.variableValueEquals("numeroDocumento", identificacion)
					.includeProcessVariables()
					//.unfinished()
					.listPage(primerItem, cantidadItems);
					//.list();
	        
	        long totalTareas = runtimeService.createProcessInstanceQuery()  	// Se valida los procesos creados para dicho numero de identificacion de asegurado.
					.variableValueEquals("numeroDocumento", identificacion)
					//.unfinished()
					.count();
	        
	        System.out.println(procesos);
	        System.out.println(totalTareas);
	        
			if (totalTareas>0) {
				
				for (ProcessInstance proceso : procesos) {
					Procesos oProceso = new Procesos();

					String idConsecutivo = (String) proceso.getProcessVariables().get("idConsecutivo");
					oProceso.setIdConsecutivo(idConsecutivo);
					oProceso.setIdProceso(proceso.getProcessInstanceId());
					oProceso.setFechaInicio(proceso.getStartTime());
					oProceso.setUsuarioCreador(proceso.getStartUserId());
					oProceso.setIdentificacionCliente(proceso.getProcessVariables().get("numeroDocumento").toString());
					//oProceso.setNumeroPoliza(proceso.getProcessVariables().get("numeroPoliza").toString());
					oProcesoList.add(oProceso);

					System.out.println("\n" + "IdProceso: " + proceso.getId() + " Variables: " + proceso.getProcessVariables()
							+ " FechaInicio: " + proceso.getStartTime() + " UsuarioCreador: " + proceso.getStartUserId());
				}
				
		        oResponseWS.setTotalItems(totalTareas);
		        oResponseWS.setListaResultado(oProcesoList);
		    	oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
			} else {
		    	oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
		    	oResponseWS.setMensaje("No se encontraron datos para la identificacion: " + identificacion);
			}

	        
		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo tareas " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
			log.error("Error obteniendo tareas " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
			
		}        
        
		return oResponseWS;
	}

	@Override
	public ResponseWS obtenerRutaProceso(String IdProceso) {		//VERIFICAR LAS TAREAS POR LAS QUE HA PASADO UNA SOLICITUD

		ResponseWS oResponseWS = new ResponseWS();
    	
    	List<Object> oProcesoList = new ArrayList<Object>();
    	
        HistoryService historyService = processEngine.getHistoryService();
        
        String DefinicionProceso = null;

		try {			
			
	        List<HistoricActivityInstance> activities =
	                historyService
	                        .createHistoricActivityInstanceQuery()
	                        .processInstanceId(IdProceso)
	                        //.unfinished()
	                        .orderByHistoricActivityInstanceStartTime()
	                        .asc()
	                        .list();
	
	        for (HistoricActivityInstance activity : activities) {
	        	
	        	//if (activity.getActivityName() != null) {
	            	Actividad oActividad = new Actividad();
	            	oActividad.setIdActividad(activity.getActivityId());
	            	oActividad.setNombre(activity.getActivityName());
	            	oActividad.setDuracion(activity.getDurationInMillis());
	            	oActividad.setUsuarioAsignado(activity.getAssignee());
	            	oActividad.setFechaInicio(activity.getStartTime());
	            	oActividad.setFechaFin(activity.getEndTime());
	            	oActividad.setTipoActividad(activity.getActivityType());
	            	oActividad.setOrdenActividad(activity.getTransactionOrder());
	            	oActividad.setProcessDefinitionId(activity.getProcessDefinitionId());
	            	DefinicionProceso = activity.getProcessDefinitionId();
	            	oProcesoList.add(oActividad);
	            	
//	                System.out.println("\n" + "IdProceso: " + activity.getProcessInstanceId() 
//	                					+ "\n /Asignado: " + activity.getAssignee() 
//	                					+ "\n /TaskId: " + activity.getTaskId()
//	                					+ "\n /Nombre: " + activity.getActivityName() 
//	                					+ "\n /Tiempo: " + activity.getDurationInMillis() + " milliseconds " 
//	                					+ "\n /Inicio: " + activity.getStartTime() 
//	                					+ "\n /Fin: " + activity.getEndTime() 
//	                					+ "\n /Tipo: " + activity.getActivityType()
//	                					+ "\n /TransaccionOrden: " + activity.getTransactionOrder()
//	                					+ "\n /processDefinitionId: " + activity.getProcessDefinitionId());
	                			
				//}
	        	
	
	        }
	        
	        RepositoryService repositoryService = processEngine.getRepositoryService();
	        BpmnModel bpmnModel = repositoryService.getBpmnModel(DefinicionProceso);

	        String xml = StreamUtils.copyToString(repositoryService.getProcessModel(DefinicionProceso), StandardCharsets.UTF_8);

	        

	        
	        oResponseWS.setMensaje(xml);
	        oResponseWS.setListaResultado(oProcesoList);
	    	oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
	        
		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error historico de proceso " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
			log.error("Error historico de proceso " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
			
		} 
        
		return oResponseWS;	

	}

	@Override
	public ResponseWS obtenerInfoProcesoDetalle(String idProceso) {		//Obtener los detalles de negocio para las tareas asiganadas al usuario loggeado.

		ResponseWS oResponseWS = new ResponseWS();
		String definicionProceso = null;
    	
		try {
			HistoricProcessInstance datosProceso = processEngine.getHistoryService()
											.createHistoricProcessInstanceQuery()
											.processInstanceId(idProceso)
											//.includeProcessVariables()
											.list()
											.get(0);
			
			
			RepositoryService repositoryService = processEngine.getRepositoryService();
			definicionProceso = datosProceso.getProcessDefinitionId();
			String xml = StreamUtils.copyToString(repositoryService.getProcessModel(definicionProceso), StandardCharsets.UTF_8);
			
			Optional<InfoGeneralProceso> procesoIndemnizacion = infoProcesoRepository.findByIdProceso(idProceso);
			
            Map<String, Object> map = new LinkedHashMap<>();
            Map<String, Object> metadataMap = new LinkedHashMap<>();

            ObjCodigoValor canalCreacion = (ObjCodigoValor) procesoIndemnizacion.get().getDocumento().getInfoProceso().getCanalCreacion(); //datosProceso.getProcessVariables().get("canalCreacion");
            
            String estadoCaso = procesoIndemnizacion.get().getDocumento().getInfoProceso().getEstadoSolicitud();
            
            metadataMap.put("idConsecutivo", procesoIndemnizacion.get().getDocumento().getInfoProceso().getIdConsecutivo());
            metadataMap.put("fechaInicio", datosProceso.getStartTime());
            metadataMap.put("usuarioCreador", datosProceso.getStartUserId());
            metadataMap.put("canalCreacion", canalCreacion);
            metadataMap.put("estadoCaso",estadoCaso);
            
            
            VariablesProceso oVariablesProceso = procesoIndemnizacion.get().getDocumento();        
            
            
            map.put("xml", xml);
            map.put("idProceso", datosProceso.getId());
            map.put("metadata", metadataMap);
            map.put("infoProceso", procesoIndemnizacion.get().getDocumento().getInfoProceso());
            map.put("asegurado", procesoIndemnizacion.get().getDocumento().getAsegurado());
            map.put("infoProducto", procesoIndemnizacion.get().getDocumento().getInfoProducto());
            map.put("siniestro", procesoIndemnizacion.get().getDocumento().getSiniestro());
            map.put("canalAtencion", procesoIndemnizacion.get().getDocumento().getCanalAtencion());
            map.put("pago", procesoIndemnizacion.get().getDocumento().getPago());
            map.put("anulacion", procesoIndemnizacion.get().getDocumento().getAnulacion());
            map.put("objecion", procesoIndemnizacion.get().getDocumento().getObjecion());
            map.put("ajustador", procesoIndemnizacion.get().getDocumento().getAjustador());	
            
			
	        oResponseWS.setResultado(map);
	    	oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
	        
		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo datos del proceso " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
			log.error("Error obteniendo datos del proceso " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
		}
		
		return oResponseWS;
	}

	@Override
	public ResponseWS obtenerListaProcesosHistorico(Boolean procesosFinalizados, Integer cantidadItems, Integer primerItem) { //Mostrar todos los proceso ya finalizos o con tareas asignadas a usuarios
		
		ResponseWS oResponseWS = new ResponseWS();		
		
		List<Object> oProcesoList = new ArrayList<Object>();
		
		long totalProcesos = 0;
		
		try {
			
			if (procesosFinalizados == false) { //Se listan solo procesos abiertos y asignados
				
				HistoryService historyService = processEngine.getHistoryService();
				
				List<HistoricTaskInstance> procesos = historyService.createHistoricTaskInstanceQuery() 	// Se obtienen los procesos NO finalizados
						//.variableValueEquals("numeroDocumento", identificacion)
						
						.includeProcessVariables()
						.unfinished()
						.orderByHistoricTaskInstanceStartTime()
						.desc()
						.listPage(primerItem, cantidadItems);
						//.list();
		        
		        totalProcesos = historyService.createHistoricTaskInstanceQuery()	// Se valida los procesos creados para dicho numero de identificacion de asegurado.
						//.variableValueEquals("numeroDocumento", identificacion)
						.unfinished()
						.count();
		        
		        for (HistoricTaskInstance proceso : procesos) {
		        	
		            Map<String, Object> map = new LinkedHashMap<>();
		            map.put("idConsecutivo", proceso.getProcessVariables().get("idConsecutivo"));
		            map.put("idProceso", proceso.getProcessInstanceId());
		            //map.put("idTarea", proceso.getId());
		            //map.put("idTareaDefinicion", proceso.getTaskDefinitionId());
		            map.put("nombreTarea", proceso.getName());
		            map.put("fechaCreacion", proceso.getCreateTime());
		            map.put("fechaSolucion", proceso.getEndTime());
		            
		            oProcesoList.add(map);
		        
		        }
				
//		        List<Task> tareas = taskService.createTaskQuery()
//		                //.taskAssignee(nombreUsuario)
//		        		//.taskName(nombreTarea)
//		                .includeProcessVariables()
//		                .orderByTaskCreateTime()
//		                .desc()
//		                //.list();
//		                .listPage(primerItem, cantidadItems);
//		        
//		        totalProcesos = taskService.createTaskQuery()
//		                //.taskAssignee(nombreUsuario)
//		        		//.taskName(nombreTarea)
//		        		.count();     
//	        
//		        System.out.println(tareas);
//		        
//		        for (Task tarea : tareas) {		
//	
//		            Map<String, Object> map = new LinkedHashMap<>();
//		            map.put("idConsecutivo", tarea.getProcessVariables().get("idConsecutivo"));
//		            map.put("idProceso", tarea.getProcessInstanceId());
//		            map.put("idTarea", tarea.getId());            
//		            map.put("nombreTarea", tarea.getName());
//		            map.put("fechaCreacion", tarea.getCreateTime());
//		            map.put("fechaSolucion", tarea.getCreateTime());
//		            
//		            oProcesoList.add(map);
//		        
//		        }
			}else {		//Se listan solo procesos ya finalizados, no anulados.
				
				HistoryService historyService = processEngine.getHistoryService();
				
				List<HistoricProcessInstance> procesos = historyService.createHistoricProcessInstanceQuery() 	// Se obtienen los procesos ya finalizados
						//.variableValueEquals("numeroDocumento", identificacion)
						.includeProcessVariables()
						.finished()
						.notDeleted()
						.listPage(primerItem, cantidadItems);
						//.list();
		        
		        totalProcesos = historyService.createHistoricProcessInstanceQuery() 	// Se valida los procesos creados para dicho numero de identificacion de asegurado.
						//.variableValueEquals("numeroDocumento", identificacion)
						.finished()
						.notDeleted()
						.count();
		        
		        for (HistoricProcessInstance proceso : procesos) {
		        	
		            Map<String, Object> map = new LinkedHashMap<>();
		            map.put("idConsecutivo", proceso.getProcessVariables().get("idConsecutivo"));
		            map.put("idProceso", proceso.getId());
		            map.put("idTarea", null);            
		            map.put("nombreTarea", proceso.getEndActivityId());
		            map.put("fechaCreacion", proceso.getStartTime());
		            map.put("fechaSolucion", proceso.getEndTime());
		            
		            oProcesoList.add(map);
		        
		        }
			}
	        
	        oResponseWS.setTotalItems(totalProcesos);
	        oResponseWS.setListaResultado(oProcesoList);
	    	oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
	    	oResponseWS.setMensaje("Consulta realizada con éxito");
	        
		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo historico procesos " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
			log.error("Error obteniendo historico procesos " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
			
		}        
        
		return oResponseWS;
	}

	@Override
	public ResponseWS obtenerListaProcesosAnulados(Integer cantidadItems, Integer primerItem) {

		ResponseWS oResponseWS = new ResponseWS();		
		
		List<Object> oProcesoList = new ArrayList<Object>();

		HistoryService historyService = processEngine.getHistoryService();
		
		try {
				
			List<HistoricProcessInstance> procesos = historyService.createHistoricProcessInstanceQuery() 	// Se obtienen los procesos ya finalizados
					//.variableValueEquals("numeroDocumento", identificacion)
					.includeProcessVariables()
					.deleted()
					.listPage(primerItem, cantidadItems);
					//.list();
	        
	        long totalProcesos = historyService.createHistoricProcessInstanceQuery() 	// Se valida los procesos creados para dicho numero de identificacion de asegurado.
					//.variableValueEquals("numeroDocumento", identificacion)
					.deleted()
					.count();
	        
	        for (HistoricProcessInstance proceso : procesos) {
	        	
	            Map<String, Object> map = new LinkedHashMap<>();
	            map.put("idConsecutivo", proceso.getProcessVariables().get("idConsecutivo"));
	            //map.put("anulacion", (Anulacion) proceso.getProcessVariables().get("Anulacion"));
	            map.put("motivoAnulacion", proceso.getDeleteReason());
	            map.put("fechaAnulacion", proceso.getEndTime());
	            
	            oProcesoList.add(map);
	        
	        }
		    oResponseWS.setTotalItems(totalProcesos);
		    oResponseWS.setListaResultado(oProcesoList);
			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
			oResponseWS.setMensaje("Consulta realizada con éxito");
		
		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo procesos anulados " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
			log.error("Error obteniendo procesos anulados " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
	
		}        
	
		return oResponseWS;
			
	}

	@Override
	public ResponseWS obtenerListaProcesosPendientes(Integer cantidadItems, Integer primerItem) {
		
		ResponseWS oResponseWS = new ResponseWS();		
		
		List<Object> oProcesoList = new ArrayList<Object>();
		
		HistoryService historyService = processEngine.getHistoryService();
		
		try {
		
			List<EventSubscription> procesos = runtimeService.createEventSubscriptionQuery()	// Se obtienen los procesos ya finalizados
					//.variableValueEquals("numeroDocumento", identificacion)
					//.includeProcessVariables()
					.orderByCreateDate()
					.desc()
					.listPage(primerItem, cantidadItems);
					//.list();
	        
	        long totalProcesos = runtimeService.createEventSubscriptionQuery()	// Se valida los procesos creados para dicho numero de identificacion de asegurado.
					//.variableValueEquals("numeroDocumento", identificacion)
					//.finished()
					.count();
	        
	        for (EventSubscription proceso : procesos) {
	        	
	        	 ProcessInstance resultado = runtimeService.createProcessInstanceQuery()
						.processInstanceId(proceso.getProcessInstanceId())
						.includeProcessVariables().singleResult();

	            Map<String, Object> map = new LinkedHashMap<>();
	            map.put("idConsecutivo", resultado.getProcessVariables().get("idConsecutivo"));
	            map.put("idProceso", proceso.getProcessInstanceId());
	            map.put("idTareaDefinicion", proceso.getActivityId());
	            map.put("nombreTarea", proceso.getEventName());
	            map.put("fechaCreacion", proceso.getCreated());
	            map.put("fechaSolucion", null);
	            
	            oProcesoList.add(map);
	        
	        }
		    oResponseWS.setTotalItems(totalProcesos);
		    oResponseWS.setListaResultado(oProcesoList);
			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
			oResponseWS.setMensaje("Consulta realizada con éxito");
        
        } catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo procesos anulados " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
			log.error("Error obteniendo procesos anulados " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getLocalizedMessage());
	
		}        
	
		return oResponseWS;
	}

	
    public ResponseWS completarTarea(String idTarea, String idTareaDefinicion, VariablesProceso variablesProceso) {		//Completar la tarea creada para el usuario/analista
        
    	ResponseWS oResponseWS = new ResponseWS();		
    	oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
    	oResponseWS.setMensaje("Tarea completada exitosamente");
    	//TareaDefinicion tarea = TareaDefinicion.valueOf(idTareaDefinicion);
    	Map<String, Object> variables = new HashMap<String, Object>();
    	System.out.println(idTareaDefinicion);
    	
    	try {
    		switch (idTareaDefinicion) {
    		case "userTask_CreacionCasoStellent":
    			
    			break;
    		case "userTask_ConceptoAnalisisCaso":
    			
    			break;
    		case "userTask_ActualizacionValorReserva":
    			System.out.println(idTareaDefinicion);
    			log.info("objecto: " + variablesProceso);
    			String tipoEvento = variablesProceso.getSiniestro().getTipoEvento();
    			Boolean reservaActualizada = actualizarValorReservaService.consultaServicioSimon(variablesProceso, tipoEvento);
    			log.info(reservaActualizada.toString());
    			
    			if (reservaActualizada) {
        			Long valorReserva = variablesProceso.getSiniestro().getValorReserva();

        			log.info(tipoEvento, valorReserva, idTareaDefinicion);
        			variables.put("valorReserva", valorReserva);        			
        	        taskService.complete(idTarea, variables);        	        
				} else {
	    			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
	    			oResponseWS.setMensaje("El valor de la reserva no se ha modificado, por favor valide ");
	    			log.error("El valor de la reserva no se ha modificado, por favor valide ");
				}

    	        
    			
    			break;
    		case "userTask_ValidacionLiquidacion":
    			
    			break;
    		case "userTask_LevantamientoControlSimon":
    			
    			break;
    		case "userTask_ExplicacionCasoObjetado":
    			
    			break;
    		case "userTask_AnalisisCasoObjetado":
    			
    			break;
    		default:
    			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
    			oResponseWS.setMensaje("Ninguna tarea enviada por completar " + idTareaDefinicion);
    			log.info("Ninguna tarea enviada por completar " + idTareaDefinicion);
    			break;
    		}
		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error completando la tarea " + idTareaDefinicion);
			log.error("Error completando la tarea " + idTareaDefinicion + e.getMessage() + " | " + e.getClass() + " | " + e.getCause());
		}
    	
    	
    	
    	
    	
    	return oResponseWS;   
        

    }
	

}
