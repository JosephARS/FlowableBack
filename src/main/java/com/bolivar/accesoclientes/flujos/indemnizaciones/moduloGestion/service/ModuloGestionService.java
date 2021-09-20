package com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ExecutionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.eventsubscription.api.EventSubscription;
import org.flowable.identitylink.api.IdentityLinkInfo;
import org.flowable.identitylink.api.IdentityLinkType;
import org.flowable.job.api.Job;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.bolivar.accesoclientes.flujos.indemnizaciones.actualizarValorReserva.service.ActualizarValorReservaService;
import com.bolivar.accesoclientes.flujos.indemnizaciones.asignarUsuarioTarea.model.Usuario;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.Procesos;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;
import com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.DAO.ModuloGestionDAO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.model.Actividad;
import com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.model.TareasCantidad;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Ajustador;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Analisis;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.CanalAtencion;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.InfoGeneralProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.InfoProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.ObjCodigoValor;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Objecion;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Pago;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.VariablesProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.UsuariosRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import liquibase.pro.packaged.a;
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
	ManagementService managementService;
	UsuariosRepository usuariosRepository;
	ActualizarValorReservaService actualizarValorReservaService;

	@Override
	public ResponseWS obtenerTareasUsuario(String nombreUsuario, String nombreTarea, Integer itemPorPagina,
			Integer primerItem) {

		ResponseWS oResponseWS = new ResponseWS();

		List<Object> oTareaList = new ArrayList<Object>();
		
		List<Task> tareas;
		
		long totalTareas = 0;
		
		Date fechaActual = new Date();

		try {
			
			if (nombreTarea.equals("Todas")) {				//Retornar todas las tareas asignadas a un usuario.
				System.out.println(nombreUsuario + nombreTarea);
				tareas = taskService.createTaskQuery().taskAssignee(nombreUsuario)//.taskAssigneeLikeIgnoreCase(nombreUsuario)
						.includeProcessVariables().orderByTaskCreateTime().desc()
						// .list();
						.listPage(primerItem, itemPorPagina);

				totalTareas = taskService.createTaskQuery().taskAssignee(nombreUsuario).count();
				
			} else {										//Retornar tareas de un nombre especifico asignadas a un usuario.
				tareas = taskService.createTaskQuery().taskAssignee(nombreUsuario).taskName(nombreTarea)
						.includeProcessVariables().orderByTaskCreateTime().desc()
						// .list();
						.listPage(primerItem, itemPorPagina);

				totalTareas = taskService.createTaskQuery().taskAssignee(nombreUsuario).taskName(nombreTarea).count();
			}

			System.out.println(tareas);

			for (Task tarea : tareas) {

				Map<String, Object> map = new LinkedHashMap<>();
				map.put("idConsecutivo", tarea.getProcessVariables().get("idConsecutivo"));
				map.put("idProceso", tarea.getProcessInstanceId());
				map.put("idTarea", tarea.getId());
				map.put("idTareaDefinicion", tarea.getTaskDefinitionKey());
				map.put("nombreTarea", tarea.getName());
				map.put("fechaCreacion", tarea.getCreateTime());
				map.put("fechaSolucion", tarea.getDueDate());
				map.put("estadoSolucion", calcularTiempoSolucion(fechaActual, tarea.getDueDate()));

				oTareaList.add(map);

			}
			
			if (totalTareas>0) {
				oResponseWS.setTotalItems(totalTareas);
				oResponseWS.setListaResultado(oTareaList);
				oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
			}else {
				oResponseWS.setTotalItems(totalTareas);
				oResponseWS.setMensaje("No se encontraron datos para el usuario: " + nombreUsuario );
				oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			}
			


		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo tareas " );
			log.error("Error obteniendo tareas " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);

		}

		return oResponseWS;

	}

	@Override
	public ResponseWS cantidadTareasUsuario(String nombreUsuario) {

		ResponseWS oResponseWS = new ResponseWS();

		try {
			List<Task> tareas = taskService.createTaskQuery().taskAssignee(nombreUsuario).list();

			System.out.println("nombreUsuario:" + nombreUsuario);
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
			});

			Map<Object, Long> couterMap = conteoTareas.stream()
					.collect(Collectors.groupingBy(e -> e.toString(), Collectors.counting())); // Agrupa y cuentas la
																								// cantidad de tareas
			System.out.println("CounterMap" + couterMap);

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
			oResponseWS.setMensaje("Error obteniendo cantidad tareas ");
			log.error("Error obteniendo cantidad tareas " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
		}

		return oResponseWS;

	}

	@Override
	public ResponseWS obtenerProcesosIdentificacion(String identificacion, Integer itemPorPagina, Integer primerItem) {

		ResponseWS oResponseWS = new ResponseWS();

		List<Object> oProcesoList = new ArrayList<Object>();

		HistoryService historyService = processEngine.getHistoryService();

		try {
			List<ProcessInstance> procesos = runtimeService.createProcessInstanceQuery() // Se valida los procesos
																							// creados para dicho numero
																							// de identificacion de
																							// asegurado.
					.variableValueEquals("numeroDocumento", identificacion).includeProcessVariables()
					// .unfinished()
					.listPage(primerItem, itemPorPagina);
			// .list();

			long totalTareas = runtimeService.createProcessInstanceQuery() // Se valida los procesos creados para dicho
																			// numero de identificacion de asegurado.
					.variableValueEquals("numeroDocumento", identificacion)
					// .unfinished()
					.count();

			System.out.println(procesos);
			System.out.println(totalTareas);

			if (totalTareas > 0) {

				for (ProcessInstance proceso : procesos) {
					Procesos oProceso = new Procesos();

					String idConsecutivo = (String) proceso.getProcessVariables().get("idConsecutivo");
					oProceso.setIdConsecutivo(idConsecutivo);
					oProceso.setIdProceso(proceso.getProcessInstanceId());
					oProceso.setFechaInicio(proceso.getStartTime());
					oProceso.setUsuarioCreador(proceso.getStartUserId());
					oProceso.setIdentificacionCliente(proceso.getProcessVariables().get("numeroDocumento").toString());
					// oProceso.setNumeroPoliza(proceso.getProcessVariables().get("numeroPoliza").toString());
					oProcesoList.add(oProceso);

					System.out.println("\n" + "IdProceso: " + proceso.getId() + " Variables: "
							+ proceso.getProcessVariables() + " FechaInicio: " + proceso.getStartTime()
							+ " UsuarioCreador: " + proceso.getStartUserId());
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
			oResponseWS.setMensaje("Error obteniendo tareas " );
			log.error("Error obteniendo tareas " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);

		}

		return oResponseWS;
	}

	@Override
	public ResponseWS obtenerRutaProceso(String IdProceso) { // VERIFICAR LAS TAREAS POR LAS QUE HA PASADO UNA SOLICITUD

		ResponseWS oResponseWS = new ResponseWS();

		List<Object> oProcesoList = new ArrayList<Object>();

		HistoryService historyService = processEngine.getHistoryService();

		String DefinicionProceso = null;

		try {

			List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
					.processInstanceId(IdProceso)
					// .unfinished()
					.orderByHistoricActivityInstanceStartTime().asc().list();

			for (HistoricActivityInstance activity : activities) {

				// if (activity.getActivityName() != null) {
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

			}

			RepositoryService repositoryService = processEngine.getRepositoryService();
			InputStream bpmnModel = repositoryService.getProcessModel(DefinicionProceso);

			String xml = StreamUtils.copyToString(bpmnModel, StandardCharsets.UTF_8);

			oResponseWS.setMensaje(xml);
			oResponseWS.setListaResultado(oProcesoList);
			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);

		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error historico de proceso " );
			log.error("Error historico de proceso " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);

		}

		return oResponseWS;

	}

	@Override
	public ResponseWS obtenerInfoProcesoDetalle(String idProceso) { // Obtener los detalles de negocio para las tareas
																	// asiganadas al usuario loggeado.

		ResponseWS oResponseWS = new ResponseWS();
		String definicionProceso = null;

		try {
			HistoricProcessInstance datosProceso = processEngine.getHistoryService()
					.createHistoricProcessInstanceQuery().processInstanceId(idProceso)
					// .includeProcessVariables()
					.list().get(0);

			RepositoryService repositoryService = processEngine.getRepositoryService();
			definicionProceso = datosProceso.getProcessDefinitionId();
			String xml = StreamUtils.copyToString(repositoryService.getProcessModel(definicionProceso),
					StandardCharsets.UTF_8);

			Optional<InfoGeneralProceso> procesoIndemnizacion = infoProcesoRepository.findByIdProceso(idProceso);

			Map<String, Object> map = new LinkedHashMap<>();
			Map<String, Object> metadataMap = new LinkedHashMap<>();
			ObjCodigoValor canalCreacion = null;
			String estadoCaso = "";
			if (procesoIndemnizacion.isPresent()) {
				canalCreacion =  procesoIndemnizacion.get().getDocumento().getInfoProceso()
						.getCanalCreacion();
				estadoCaso = procesoIndemnizacion.get().getDocumento().getInfoProceso().getEstadoSolicitud();
			}
			

			

			metadataMap.put("idConsecutivo",
					procesoIndemnizacion.get().getDocumento().getInfoProceso().getIdConsecutivo());
			metadataMap.put("fechaInicio", datosProceso.getStartTime());
			metadataMap.put("usuarioCreador", datosProceso.getStartUserId());
			metadataMap.put("canalCreacion", canalCreacion);
			metadataMap.put("estadoCaso", estadoCaso);

			//VariablesProceso oVariablesProceso = procesoIndemnizacion.get().getDocumento();

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
			map.put("historialAnalisis", procesoIndemnizacion.get().getHistorialAnalisis());

			oResponseWS.setResultado(map);
			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);

		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo datos del proceso " );
			log.error("Error obteniendo datos del proceso " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
		}

		return oResponseWS;
	}

	@Override
	public ResponseWS obtenerListaProcesosHistorico(Boolean procesosFinalizados, Integer itemPorPagina, Integer primerItem, Date fechaInicio, Date fechaFin) { // Mostrar todos los proceso ya finalizados o con tareas asignadas a usuarios

		ResponseWS oResponseWS = new ResponseWS();

		List<Object> oProcesoList = new ArrayList<Object>();

		long totalProcesos = 0;
		
		Date fechaActual = new Date();
		
		System.out.println(fechaInicio + "/"+ fechaFin);

		try {

			if (procesosFinalizados == false) { // Se listan solo procesos abiertos y asignados

				HistoryService historyService = processEngine.getHistoryService();

				List<HistoricTaskInstance> procesos = historyService.createHistoricTaskInstanceQuery() // Se obtienen
																										// los procesos
																										// NO
																										// finalizados
						.includeProcessVariables()
						.unfinished()
						.taskCreatedBefore(fechaFin)
						.taskCreatedAfter(fechaInicio)
						.orderByHistoricTaskInstanceStartTime().desc()
						.listPage(primerItem, itemPorPagina);


				totalProcesos = historyService.createHistoricTaskInstanceQuery() // Se valida los procesos creados para
																					// dicho numero de identificacion de
																					// asegurado.
						.taskCreatedBefore(fechaFin)
						.taskCreatedAfter(fechaInicio)
						.unfinished().count();

				for (HistoricTaskInstance proceso : procesos) {

					Map<String, Object> map = new LinkedHashMap<>();
					map.put("idConsecutivo", proceso.getProcessVariables().get("idConsecutivo"));
					map.put("idProceso", proceso.getProcessInstanceId());
					map.put("nombreTarea", proceso.getName());
					map.put("fechaCreacion", proceso.getCreateTime());
					map.put("fechaSolucion", proceso.getDueDate());
					map.put("estadoSolucion", calcularTiempoSolucion(fechaActual, proceso.getDueDate()));
					oProcesoList.add(map);

				}

			} else { // Se listan solo procesos ya finalizados, no anulados.

				HistoryService historyService = processEngine.getHistoryService();

				List<HistoricProcessInstance> procesos = historyService.createHistoricProcessInstanceQuery() // Se obtienen los procesos ya finalizados
						.includeProcessVariables()
						.finished()
						.notDeleted()
						.finishedBefore(fechaFin)
						.finishedAfter(fechaInicio)
						.orderByProcessInstanceEndTime()
						.desc()
						.listPage(primerItem, itemPorPagina);

				totalProcesos = historyService.createHistoricProcessInstanceQuery() // Se valida los procesos creados para dicho numero de identificacion de asegurado.
						.finished()
						.notDeleted()
						.finishedBefore(fechaFin)
						.finishedAfter(fechaInicio)
						.count();

				
				for (HistoricProcessInstance proceso : procesos) {

					Map<String, Object> map = new LinkedHashMap<>();
					map.put("idConsecutivo", proceso.getProcessVariables().get("idConsecutivo"));
					map.put("idProceso", proceso.getId());
					map.put("idTarea", null);
					map.put("nombreActividad", proceso.getEndActivityId());
					map.put("fechaCreacion", proceso.getStartTime());
					map.put("fechaSolucion", proceso.getEndTime());
					map.put("tiempoSolucion", proceso.getDurationInMillis());
					oProcesoList.add(map);

				}
			}
			
			if (totalProcesos>0) {
				oResponseWS.setTotalItems(totalProcesos);
				oResponseWS.setListaResultado(oProcesoList);
				oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
				oResponseWS.setMensaje("Consulta realizada con exito");
			}else {
				oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
				oResponseWS.setMensaje("No se encontraron resultados para la consulta.");
			}

		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo historico procesos " );
			log.error("Error obteniendo historico procesos " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);

		}

		return oResponseWS;
	}

	@Override
	public ResponseWS obtenerListaProcesosAnulados(Integer itemPorPagina, Integer primerItem, Date fechaInicio, Date fechaFin) {

		ResponseWS oResponseWS = new ResponseWS();

		List<Object> oProcesoList = new ArrayList<Object>();

		HistoryService historyService = processEngine.getHistoryService();
		
		try {		
					
			List<HistoricProcessInstance> procesos = historyService.createHistoricProcessInstanceQuery() // Se obtienen los procesos ya	finalizados
					// .variableValueEquals("numeroDocumento", identificacion)
					.includeProcessVariables()
					.finished()
					.deleted()
					.finishedBefore(fechaFin)
					.finishedAfter(fechaInicio)
					.orderByProcessInstanceEndTime()
					.desc()
					.listPage(primerItem, itemPorPagina);
			
			long totalProcesos = historyService.createHistoricProcessInstanceQuery() // Se valida los procesos creados
																						// para dicho numero de
																						// identificacion de asegurado.
					// .variableValueEquals("numeroDocumento", identificacion)
					.finished()
					.deleted()
					.finishedBefore(fechaFin)
					.finishedAfter(fechaInicio)
					.count();

			
			List<String> listaIdProcesos = new ArrayList<String>();
			
			if (totalProcesos>0) {

				procesos.forEach(proceso -> {
					listaIdProcesos.add(proceso.getId());
		        });
				
				List<InfoGeneralProceso> listaResultado = infoProcesoRepository.findByIdProcesoIn(listaIdProcesos);

				for (HistoricProcessInstance proceso : procesos) {
					
					Optional<InfoGeneralProceso> anulacion = listaResultado.stream().filter(p -> p.getIdProceso().equals(proceso.getId())).findAny();
					
					Map<String, Object> map = new LinkedHashMap<>();
					map.put("idConsecutivo", proceso.getProcessVariables().get("idConsecutivo"));
					map.put("motivoAnulacion", proceso.getDeleteReason());
					map.put("fechaCreacion", proceso.getStartTime());
					map.put("fechaAnulacion", proceso.getEndTime());
					if (anulacion.isPresent()) {
						map.put("anulacion", anulacion.get().getDocumento().getAnulacion());
					}

	
					oProcesoList.add(map);
	
				}
				oResponseWS.setTotalItems(totalProcesos);
				oResponseWS.setListaResultado(oProcesoList);
				oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
				oResponseWS.setMensaje("Consulta realizada con exito");
			}else {
				oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
				oResponseWS.setMensaje("No se encontraron resultados para la consulta.");
			}

		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo procesos anulados " );
			log.error("Error obteniendo procesos anulados " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);

		}

		return oResponseWS;

	}

	@Override
	public ResponseWS obtenerListaProcesosPendientes(Integer itemPorPagina, Integer primerItem, Date fechaInicio, Date fechaFin) {

		ResponseWS oResponseWS = new ResponseWS();

		List<Object> oProcesoList = new ArrayList<Object>();

		HistoryService historyService = processEngine.getHistoryService();
		
		List<EventSubscription> procesos = null;
		
		long totalProcesos = 0;

		try {
			if (fechaFin == null) {
				procesos = runtimeService.createEventSubscriptionQuery() // Se obtienen los procesos ya finalizados
						.orderByCreateDate().desc().listPage(primerItem, itemPorPagina);


				totalProcesos = runtimeService.createEventSubscriptionQuery() // Se valida los procesos creados para dicho numero de identificacion de asegurado.
						.count();
			}else {
				procesos = runtimeService.createEventSubscriptionQuery() // Se obtienen los procesos ya finalizados
						.createdBefore(fechaFin)
						.createdAfter(fechaInicio)
						.orderByCreateDate().desc().listPage(primerItem, itemPorPagina);
	
	
				totalProcesos = runtimeService.createEventSubscriptionQuery() // Se valida los procesos creados para dicho numero de identificacion de asegurado.
						.createdBefore(fechaFin)
						.createdAfter(fechaInicio)
						.count();
			}
			
			if (totalProcesos>0) {
				for (EventSubscription proceso : procesos) {

					ProcessInstance resultado = runtimeService.createProcessInstanceQuery()
							.processInstanceId(proceso.getProcessInstanceId()).includeProcessVariables().singleResult();

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
				oResponseWS.setMensaje("Consulta realizada con exito");
			}else {
				oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
				oResponseWS.setMensaje("No se encontraron resultados para la consulta.");
			}
			

		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo procesos pendientes ");
			log.error("Error obteniendo procesos pendientes " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);

		}

		return oResponseWS;
	}
	

	public ResponseWS completarTarea(String idTarea, String idTareaDefinicion, String idUsuario,
			VariablesProceso variablesProceso) { // Completar la tarea creada para el usuario/analista

		ResponseWS oResponseWS = new ResponseWS();
		oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
		oResponseWS.setMensaje("Tarea completada exitosamente");
		// TareaDefinicion tarea = TareaDefinicion.valueOf(idTareaDefinicion);
		Map<String, Object> variables = new HashMap<String, Object>();
		System.out.println(idTareaDefinicion);
		String idProceso = variablesProceso.getInfoProceso().getIdProceso();
		String comentario = "";
		ObjectMapper mapper = new ObjectMapper();

		try {
			switch (idTareaDefinicion) {
			case "userTask_CreacionCasoStellent":

				CanalAtencion canalAtencion = variablesProceso.getCanalAtencion();
				

				try {
					log.info("procesoIndemnizacion: "+ mapper.writeValueAsString(canalAtencion));
					infoProcesoRepository.creacionCasoStellent(idProceso, mapper.writeValueAsString(canalAtencion));
				} catch (JsonProcessingException e) {
					log.error(idTareaDefinicion, e.getMessage(), e.getCause());
					e.printStackTrace();
				}
				String lineaAtencion = variablesProceso.getCanalAtencion().getLineaAtencion();
				variables.put("lineaAtencion", lineaAtencion);
				taskService.complete(idTarea, variables);
				usuariosRepository.P_TAREA_CERRADA(idUsuario, "Completada");
				
				break;
			case "userTask_ConceptoAnalisisCaso":
				
				InfoProceso infoProceso = variablesProceso.getInfoProceso();
				
				try {
					log.info("procesoIndemnizacion: "+ mapper.writeValueAsString(infoProceso));
					infoProcesoRepository.conceptoAnalisisCaso(idProceso, mapper.writeValueAsString(infoProceso));
				} catch (JsonProcessingException e) {
					log.error(idTareaDefinicion, e.getMessage(), e.getCause());
					e.printStackTrace();
				}
				ObjCodigoValor cambioMotorDef = variablesProceso.getInfoProceso().getCambioMotorDef();
				variables.put("cambioMotorDef", cambioMotorDef);
				log.info(idTarea);
				taskService.complete(idTarea, variables);
				usuariosRepository.P_TAREA_CERRADA(idUsuario, "Completada");
				
				break;
			case "userTask_ActualizacionValorReserva":
				System.out.println(idTareaDefinicion);
				log.info("objecto: " + variablesProceso);

				Boolean reservaActualizada = actualizarValorReservaService.consultaServicioSimon(variablesProceso);
				log.info(reservaActualizada.toString());

				if (reservaActualizada) {
					Long valorReserva = variablesProceso.getSiniestro().getValorReserva();

					log.info(valorReserva + idTareaDefinicion);
					variables.put("valorReserva", valorReserva);
					//taskService.addUserIdentityLink(idTarea, idUsuario, "assignee");
					taskService.complete(idTarea, variables);
					usuariosRepository.P_TAREA_CERRADA(idUsuario, "Completada");
				} else {
					oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
					oResponseWS.setMensaje("El valor de la reserva no se ha modificado, por favor valide ");
					log.error("El valor de la reserva no se ha modificado, por favor valide ");
				}

				break;
			case "userTask_ValidacionLiquidacion":
				Pago pago = variablesProceso.getPago();
				comentario = variablesProceso.getPago().getConcepto();
				try {
					log.info("procesoIndemnizacion: "+ mapper.writeValueAsString(pago));
					infoProcesoRepository.validacionLiquidacion(idProceso, mapper.writeValueAsString(pago));
				} catch (JsonProcessingException e) {
					log.error(idTareaDefinicion, e.getMessage(), e.getCause());
					e.printStackTrace();
				}
				
				taskService.addComment(idTarea, idProceso, comentario);
				taskService.complete(idTarea);
				usuariosRepository.P_TAREA_CERRADA(idUsuario, "Completada");

				break;
			case "userTask_LevantamientoControlSimon":
				Pago pago2 = variablesProceso.getPago();
				try {
					log.info("procesoIndemnizacion: "+ mapper.writeValueAsString(pago2));
					infoProcesoRepository.validacionLiquidacion(idProceso, mapper.writeValueAsString(pago2));
				} catch (JsonProcessingException e) {
					log.error(idTareaDefinicion, e.getMessage(), e.getCause());
					e.printStackTrace();
				}
				
				taskService.complete(idTarea);
				usuariosRepository.P_TAREA_CERRADA(idUsuario, "Completada");
				break;
			case "userTask_ExplicacionCasoObjetado":
				Objecion objecion = variablesProceso.getObjecion();
				comentario = variablesProceso.getObjecion().getObservacion();
				
				try {
					log.info("procesoIndemnizacion: "+ mapper.writeValueAsString(objecion));
					infoProcesoRepository.explicacionCasoObjetado(idProceso, mapper.writeValueAsString(objecion));
				} catch (JsonProcessingException e) {
					log.error(idTareaDefinicion, e.getMessage(), e.getCause());
					e.printStackTrace();
				}
				taskService.addComment(idTarea, idProceso, comentario);
				taskService.complete(idTarea);
				usuariosRepository.P_TAREA_CERRADA(idUsuario, "Completada");

				break;
			case "userTask_AnalisisCasoObjetado":
				InfoProceso infoProceso2 = variablesProceso.getInfoProceso();
				
				ObjCodigoValor resultadoMotor = variablesProceso.getInfoProceso().getResultadoMotorDefi();
				String cambioResultadoMotor = variablesProceso.getInfoProceso().getCambioMotorDef().getValor();

				if (cambioResultadoMotor == null || cambioResultadoMotor.isBlank()) {
					log.info("resultadoMotor: " + resultadoMotor);
					infoProceso2.setCambioMotorDef(resultadoMotor);
				}
				
				try {
					log.info("procesoIndemnizacion: "+ mapper.writeValueAsString(infoProceso2));
					infoProcesoRepository.conceptoAnalisisCaso(idProceso, mapper.writeValueAsString(infoProceso2));
				} catch (JsonProcessingException e) {
					log.error(idTareaDefinicion, e.getMessage(), e.getCause());
					e.printStackTrace();
				}

				ObjCodigoValor cambioMotorDef2 = variablesProceso.getInfoProceso().getCambioMotorDef();

				variables.put("cambioMotorDef", cambioMotorDef2);
				taskService.complete(idTarea, variables);
				usuariosRepository.P_TAREA_CERRADA(idUsuario, "Completada");
				break;
			case "userTask_AsignarAjustador":
				Ajustador ajustador = variablesProceso.getAjustador();
				

				try {
					log.info("procesoIndemnizacion: "+ mapper.writeValueAsString(ajustador));
					infoProcesoRepository.asignarAjustador(idProceso, mapper.writeValueAsString(ajustador));
				} catch (JsonProcessingException e) {
					log.error(idTareaDefinicion, e.getMessage(), e.getCause());
					e.printStackTrace();
				}

				taskService.complete(idTarea);
				usuariosRepository.P_TAREA_CERRADA(idUsuario, "Completada");

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
			log.error("Error completando la tarea " + idTareaDefinicion + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
		}

		return oResponseWS;

	}

	@Override
	public ResponseWS obtenerHistorialCaso(String idProceso) {

		ResponseWS oResponseWS = new ResponseWS();

		List<Object> oProcesoList = new ArrayList<Object>();

		HistoryService historyService = processEngine.getHistoryService();

		String usuario = "";
		
		String comentario = "";

		try {
			List<HistoricActivityInstance> actividades = historyService.createHistoricActivityInstanceQuery()
					.processInstanceId(idProceso)
					.orderByHistoricActivityInstanceStartTime()
					.asc()
					.orderByHistoricActivityInstanceId()
					.asc()
					.list();

			for (HistoricActivityInstance actividad : actividades) {

				Map<String, Object> map = new LinkedHashMap<>();

				if (actividad.getActivityType().equals("startEvent") && actividad.getActivityId().equals("inicioFlujoGenerales")) {
					HistoricProcessInstance proceso = historyService.createHistoricProcessInstanceQuery()
							.processInstanceId(idProceso).list().get(0);

					usuario = proceso.getStartUserId();

				} else if(actividad.getActivityType().equals("userTask")) {

					try {
						
						List<HistoricTaskInstance> tareasHistorico = historyService.createHistoricTaskInstanceQuery()
																				.taskId(actividad.getTaskId())
																				.includeIdentityLinks()
																				.list();

						for (HistoricTaskInstance tarea : tareasHistorico) {

							List<Comment> listaComentarios = taskService.getTaskComments(actividad.getTaskId());
							if (listaComentarios.size() > 0) {
								comentario = listaComentarios.get(0).getFullMessage();
							}
							
							
							for (IdentityLinkInfo idinfo : tarea.getIdentityLinks()) {

								
								if (idinfo.getType().equals(IdentityLinkType.ASSIGNEE) || idinfo.getType().equals(IdentityLinkType.CANDIDATE)) {
									usuario = (idinfo.getUserId() != null) ? idinfo.getUserId() : "";
									break;									
								}		
							}
						}						

					} catch (Exception e) {
						log.info(e.getMessage());
					}

				}
				
				map.put("orden", actividad.getTransactionOrder());
				map.put("fechaInicio", actividad.getStartTime());
				map.put("usuario", usuario);
				map.put("nombreActividad", actividad.getActivityName());
				map.put("fechaFin", actividad.getEndTime());
				map.put("tiempoSolucion", actividad.getDurationInMillis());
				map.put("tipoActividad", actividad.getActivityType());
				map.put("comentario", comentario);
				usuario = "";
				comentario="";
				oProcesoList.add(map);

			}
			oResponseWS.setListaResultado(oProcesoList);
			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);

		} catch (Exception e) {
			String mensajeError = "Error obteniendo historial del caso " + idProceso + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0];
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			log.error(mensajeError);
		}

		return oResponseWS;
	}

	@Override
	public ResponseWS busquedaGeneral(String parametroBusqueda, String valorBusqueda, Integer itemPorPagina,
			Integer primerItem) {

		ResponseWS oResponseWS = new ResponseWS();

		List<Object> oProcesoList = new ArrayList<Object>();

		System.out.println(parametroBusqueda + valorBusqueda + itemPorPagina + primerItem);
		HistoryService historyService = processEngine.getHistoryService();
		List<HistoricProcessInstance> procesos = null;
		HistoricActivityInstance actividad;
		List<ActivityInstance> actividad2;
		Set<String> listaProcesos = new HashSet<String>();
		String nombreTarea = "";
		long totalItems = 0;

		try {

			switch (parametroBusqueda) {
			case "consecutivo":

				procesos = historyService.createHistoricProcessInstanceQuery()
						.variableValueEquals("idConsecutivo", valorBusqueda).includeProcessVariables()
						.orderByProcessInstanceStartTime().desc().listPage(primerItem, itemPorPagina);

				totalItems = historyService.createHistoricProcessInstanceQuery()
						.variableValueEquals("idConsecutivo", valorBusqueda).count();

				break;

			case "numeroDocumento":

				procesos = historyService.createHistoricProcessInstanceQuery()
						.variableValueEquals("numeroDocumento", valorBusqueda).includeProcessVariables()
						.orderByProcessInstanceStartTime().desc().listPage(primerItem, itemPorPagina);

				totalItems = historyService.createHistoricProcessInstanceQuery()
						.variableValueEquals("numeroDocumento", valorBusqueda).count();

				break;

			case "numeroSiniestro":
				
				List<InfoGeneralProceso> resultadoProcesos = null;
				try {
					resultadoProcesos = infoProcesoRepository.P_BUSCAR_PROCESO(parametroBusqueda,
							valorBusqueda, itemPorPagina, primerItem);

				} catch (Exception e) {
					log.error("NO DATOS" + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
					break;
				} 

				if (resultadoProcesos.size() > 0) {
					
					resultadoProcesos.forEach(proceso -> {
						listaProcesos.add(proceso.getIdProceso());
					});
				
					procesos = historyService.createHistoricProcessInstanceQuery()
							.processInstanceIds(listaProcesos)
							.includeProcessVariables()
							.orderByProcessInstanceStartTime()
							.desc()
							.listPage(primerItem, itemPorPagina);

					totalItems = resultadoProcesos.size();
				}


				break;

			default:
				break;
			}

			if (totalItems > 0) {

				for (HistoricProcessInstance proceso : procesos) {

					if (proceso.getEndTime() != null) { // Proceso finalizado
						actividad = historyService.createHistoricActivityInstanceQuery()
								.processInstanceId(proceso.getId()).activityId(proceso.getEndActivityId())
								.orderByHistoricActivityInstanceEndTime().orderByHistoricActivityInstanceId().finished()
								.desc().list().get(0);

						nombreTarea = actividad.getActivityName();

					} else {
						actividad2 = runtimeService.createActivityInstanceQuery().processInstanceId(proceso.getId())
								.unfinished().list();

						for (ActivityInstance a : actividad2) {
							if (a.getActivityId() != null) {
								nombreTarea = a.getActivityName();
							}
							;
						}

					}

					Map<String, Object> map = new LinkedHashMap<>();
					map.put("idConsecutivo", proceso.getProcessVariables().get("idConsecutivo"));
					map.put("idProceso", proceso.getId());
					map.put("idTarea", null);
					map.put("idTareaDefinicion", null);
					map.put("nombreTarea", nombreTarea);
					map.put("fechaCreacion", proceso.getStartTime());
					map.put("fechaSolucion", proceso.getEndTime());
					map.put("tiempoSolucion", proceso.getDurationInMillis());

					oProcesoList.add(map);
					nombreTarea = "";
					System.out.println(map);

				}

				oResponseWS.setTotalItems(totalItems);
				oResponseWS.setListaResultado(oProcesoList);
				oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);

			} else {
				oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
				oResponseWS.setMensaje(
						"No se encontraron datos para el valor: " + parametroBusqueda + " " + valorBusqueda);
			}

		} catch (Exception e) {
			String mensajeError = "Error realizando busqueda "  + parametroBusqueda + " " + valorBusqueda + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0];
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje(mensajeError);
			log.error(mensajeError);
		}

		return oResponseWS;
	}
	
	
	public ResponseWS listarUsuarios(String idTareaDefinicion) {
		
		ResponseWS oResponseWS = new ResponseWS();

		List<Usuario> oListUsuarios = new ArrayList<Usuario>();
		
		try {
			oListUsuarios =  usuariosRepository.P_LISTAR_USUARIOS(idTareaDefinicion);
			List<Object> oListUsuarios2 = new ArrayList<Object>(oListUsuarios);
			
			oResponseWS.setListaResultado(oListUsuarios2);
			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
			log.info("Lista de usuarios asignables " + idTareaDefinicion);
			
		} catch (Exception e) {
			String mensajeError = "Error listando usuarios "  + idTareaDefinicion + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0];
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error listando usuarios " + idTareaDefinicion);
			log.error(mensajeError);
		}		
		return oResponseWS;		
	}
	
	
	public ResponseWS reasignarUsuario(String idProceso, String usuarioAsignado, String idTarea) {
		
		ResponseWS oResponseWS = new ResponseWS();
		
		try {
			taskService.setAssignee(idTarea, usuarioAsignado);
			taskService.addUserIdentityLink(idTarea, usuarioAsignado, IdentityLinkType.ASSIGNEE );
			taskService.addUserIdentityLink(idTarea, usuarioAsignado, IdentityLinkType.OWNER);
			taskService.addCandidateUser(idTarea, usuarioAsignado);
			
			usuariosRepository.P_REASIGNAR_USUARIO(idProceso, usuarioAsignado);
			
			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
			oResponseWS.setMensaje("Reasignacion de tarea realizada con exito");
	    	log.info("Tarea reasignada" + usuarioAsignado);
			
		} catch (Exception e) {
			String mensajeError = "Error reasignando usuario "  + usuarioAsignado + " " + idProceso + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0];
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error reasignando usuario " + usuarioAsignado);
			log.error(mensajeError);
		}		
		return oResponseWS;	
		
	}
	
	public ResponseWS cambiarEstadoUsuario(Integer idUsuario, Integer estado) {
		
		ResponseWS oResponseWS = new ResponseWS();
		System.out.println("Estado: " + idUsuario + estado);
		
		try {
			System.out.println("Estado: " + idUsuario + estado);
			int respuesta = usuariosRepository.P_CAMBIAR_ESTADO_USUARIO(idUsuario, estado);
			
			if (respuesta == 0) {
				oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
				oResponseWS.setMensaje("Cambio de estado exitoso");
		    	log.info("Cambio de estado exitoso" + idUsuario);
			} else if (respuesta == 2) {
				oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
				oResponseWS.setMensaje("No es posible desactivar todos los usuarios de un grupo/rol existente. Debe existir al menos uno activo para asignacion de tareas.");
		    	log.info("Cambio de estado exitoso" + idUsuario);
			}
			

			
		} catch (Exception e) {
			String mensajeError = "Error cambiando estado de usuario "  + idUsuario + " " + estado + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0];
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error cambiando estado de usuario ");
			log.error(mensajeError);
		}		
		return oResponseWS;	
		
	}
	
	
	public ResponseWS ingresarNuevoAnalisis(String idProceso, Analisis analisis) {
		
		ResponseWS oResponseWS = new ResponseWS();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		try {			
			int respuesta = infoProcesoRepository.P_INGRESAR_ANALISIS(idProceso, mapper.writeValueAsString(analisis));
			if (respuesta == 0) {
				oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
				oResponseWS.setMensaje("Analisis creado con exito");
		    	log.info("Analisis creado con exito");
			} else {
				oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
				oResponseWS.setMensaje("Error ingresando analisis en historial");
			}
		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			log.error("Error ingresando analisis en historial: "+ idProceso + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
		}	
		
		return oResponseWS;		
		
	}
	
	
	
	
	
	
	
	public ResponseWS corregirActividadAnterior(VariablesProceso variablesProceso) {
		
		ResponseWS oResponseWS = new ResponseWS();
		String idProceso = variablesProceso.getInfoProceso().getIdProceso();
		String idUsuario = variablesProceso.getCanalAtencion().getResponsable();
		
		runtimeService.createChangeActivityStateBuilder()
		.processInstanceId(idProceso)
		.moveActivityIdTo("userTask_CreacionCasoStellent", "userTask_ConceptoAnalisisCaso")
		.changeState();
		
		usuariosRepository.P_TAREA_CERRADA(idUsuario, "Anulada");
		
		oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
		
		return oResponseWS;
	}
	
	public Map<String, String> calcularTiempoSolucion(Date fechaInicio, Date fechaFin) {

	    Map<String, String> map = new HashMap<String, String>();
	    
	    String tiempo = "";
	    String estadoSolucion = "";
	    int tiempoAlerta = 2; //Tiempo para establecer alerta de casos prontos a vencer

	    System.out.println("FEchas: " + fechaInicio + " | " + fechaFin );
	    
	    if(fechaFin != null){
	    	
		    Long diffTime = (fechaFin.getTime() - fechaInicio.getTime());
			long h = TimeUnit.MILLISECONDS.toHours(diffTime);
			long m = Math.abs(TimeUnit.MILLISECONDS.toMinutes(diffTime) % 60);
			long s = Math.abs(TimeUnit.MILLISECONDS.toSeconds(diffTime) % 60);
			String h1 = String.format("%02d" , h);
			String m1 = String.format("%02d" , m);
			String s1 = String.format("%02d" , s);
          
         if(diffTime > 0){
        	 estadoSolucion = "Vigente";
           if(h<tiempoAlerta){
        	   estadoSolucion = "Pronto";
           }
         }else{
           estadoSolucion = "Vencido";
         }
          
	      tiempo = h1+":"+m1 +":"+s1;
	      


	    }    
	      map.put("tiempo", tiempo);
	      map.put("estado", estadoSolucion);
	      System.out.println(map);
		return map;
						
	}
	
	public ResponseWS procesosSuspendidos(Integer itemPorPagina, Integer primerItem, Date fechaInicio, Date fechaFin){ //Flowable automaticamente pone en "Dead Letter"/Suspendidos las Service task que finalizan sus reintentos y no consiguen una respuesta exitosa.
		
		ResponseWS oResponseWS = new ResponseWS();		
		
		List<Object> oProcesoList = new ArrayList<Object>();
		
		List<Job> listaProcesos = null;
		
		long totalProcesos = 0;

		try {
			
			 log.info( "Suspendidos: "+ itemPorPagina+ primerItem+ fechaInicio+fechaFin);
			 listaProcesos = managementService.createDeadLetterJobQuery()
					 							.orderByJobDuedate()
					 							.asc()
					 							.listPage(primerItem, itemPorPagina);// Se obtienen los procesos es estado "suspendido" despues de algún error en un servicio
	
	
				totalProcesos = managementService.createDeadLetterJobQuery()
							.count();
			
			if (listaProcesos.size()>0) {
				for (Job proceso : listaProcesos) {
					log.info( "Suspendidos: "+ itemPorPagina+ primerItem+ fechaInicio+fechaFin);
					ProcessInstance resultado = runtimeService.createProcessInstanceQuery()
							.processInstanceId(proceso.getProcessInstanceId()).includeProcessVariables().singleResult();

					Map<String, Object> map = new LinkedHashMap<>();
					map.put("idConsecutivo", resultado.getProcessVariables().get("idConsecutivo"));
					map.put("idProceso", proceso.getProcessInstanceId());
					map.put("fechaError", proceso.getDuedate());
					map.put("actividadError", proceso.getElementName());
					map.put("mensajeError", proceso.getExceptionMessage());
					map.put("idJob", proceso.getId());

					oProcesoList.add(map);

				}
				oResponseWS.setTotalItems(totalProcesos);
				oResponseWS.setListaResultado(oProcesoList);
				oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
				oResponseWS.setMensaje("Consulta realizada con exito");
			}else {
				oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
				oResponseWS.setMensaje("No se encontraron resultados para la consulta.");
			}
			

		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo procesos con Error WS ");
			log.error("Error obteniendo procesos con Error WS " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);

		}			

		return oResponseWS;
		
	}
	
	public ResponseWS procesosErrorWS(Integer itemPorPagina, Integer primerItem, Date fechaInicio, Date fechaFin){ 
		
		ResponseWS oResponseWS = new ResponseWS();
		
	    List<Object> oProcesoList = new ArrayList<Object>();
		
		List<Job> listaProcesos = null;
		
		long totalProcesos = 0;
	    
		try {
			
			 log.info( "Con Error WS: "+ itemPorPagina+ primerItem+ fechaInicio+fechaFin);
			 listaProcesos =  managementService.createTimerJobQuery()
					 							.orderByJobDuedate()
					 							.asc()
					 							.listPage(primerItem, itemPorPagina);// Se obtienen los procesos es estado "suspendido" despues de algún error en un servicio
	
	
				totalProcesos = managementService.createTimerJobQuery().count();
			
			if (listaProcesos.size()>0) {
				for (Job proceso : listaProcesos) {
					ProcessInstance resultado = runtimeService.createProcessInstanceQuery()
							.processInstanceId(proceso.getProcessInstanceId()).includeProcessVariables().singleResult();

					Map<String, Object> map = new LinkedHashMap<>();
					map.put("idConsecutivo", resultado.getProcessVariables().get("idConsecutivo"));
					map.put("idProceso", proceso.getProcessInstanceId());
					map.put("fechaError", proceso.getDuedate());
					map.put("actividadError", proceso.getElementName());
					map.put("mensajeError", proceso.getExceptionMessage());
					map.put("idJob", proceso.getId());

					oProcesoList.add(map);

				}
				oResponseWS.setTotalItems(totalProcesos);
				oResponseWS.setListaResultado(oProcesoList);
				oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
				oResponseWS.setMensaje("Consulta realizada con exito");
			}else {
				oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
				oResponseWS.setMensaje("No se encontraron resultados para la consulta.");
			}
			

		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo procesos con Error WS ");
			log.error("Error obteniendo procesos con Error WS " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);

		}
		
		oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
		
		return oResponseWS;
		
	}
	
	public ResponseWS reintentarProcesoSuspendido(String idJob) {
		
		ResponseWS oResponseWS = new ResponseWS();
		try {
			managementService.moveDeadLetterJobToExecutableJob(idJob, 1);
			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
			oResponseWS.setMensaje("Reintento programado con éxito ");
		} catch (Exception e) {
			log.error("Error programando reintento en proceso suspendido" + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
			oResponseWS.setMensaje("Error programando reintento en proceso suspendido");
		}

		return oResponseWS;
	}

}
