package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.DAO.CrearCasoDAO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.Consecutivos;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.Procesos;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.InfoGeneralProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.VariablesProceso;
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
public class CrearCasoService implements CrearCasoDAO {

	public static final String PROCESS_DEFINITION_KEY = "idIndemnizacionesGen";
	public static String USUARIO_CREADOR = "";
	

	RuntimeService runtimeService;
	TaskService taskService;
	ProcessEngine processEngine;
	RepositoryService repositoryService;
	IdentityService identityService;
	//ConsecutivosRepository consecutivosRepo;
	//NuevoCasoRepository nuevoCasoRepository;
	InfoGeneralProcesoRepository infoProcesoRepository;

	public void deployProcessDefinition() {

		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource("processes/IndemnizacionesGenerales.bpmn20.xml").deploy();
	}

	@Override
	public ResponseWS registrarNuevoCaso(VariablesProceso procesoIndemnizacion) {

		ResponseWS oResponseWS = new ResponseWS();
		Consecutivos oConsecutivos = new Consecutivos();
		Boolean CasoDuplicado = false;

		HistoryService historyService = processEngine.getHistoryService();		

		List<HistoricProcessInstance> procesos = historyService.createHistoricProcessInstanceQuery() 	// Se valida si ya existe un caso creado con los mismos datos.
				.variableValueEquals("numeroDocumento", procesoIndemnizacion.getAsegurado().getNumeroDocumento())
				.includeProcessVariables()
				.unfinished()
				.list();
		
		List<String> conteoProcesosActivos = new ArrayList<String>();
		
		procesos.forEach(proceso -> {
			conteoProcesosActivos.add(proceso.getId());
        });
		
		List<InfoGeneralProceso> validarProcesoRepetido = infoProcesoRepository.findByIdProcesoIn(conteoProcesosActivos);
		
		//List<InfoGeneralProceso> prueba = infoProcesoRepository.findByIdentificacionAsegurado(procesoIndemnizacion.getAsegurado().getNumeroDocumento());
		
		
		System.out.println("Procesos cantidad: " + validarProcesoRepetido.size());
		validarProcesoRepetido.clear();
		
		if (validarProcesoRepetido.size() > 0) {
			
			for (InfoGeneralProceso proceso : validarProcesoRepetido) {		//Compara que los casos encontrados sean diferentes alcaso que se va a crear.
				
				Boolean FechaSiniestroExiste = (procesoIndemnizacion.getSiniestro().getFechaSiniestro().equals(proceso.getDocumento().getSiniestro().getFechaSiniestro()));
				Boolean NumeroPolizaExiste =  (procesoIndemnizacion.getInfoProducto().getNumeroPoliza().equals(proceso.getDocumento().getInfoProducto().getNumeroPoliza()));
				Boolean CausaExiste =  (procesoIndemnizacion.getInfoProducto().getCausa().equals(proceso.getDocumento().getInfoProducto().getCausa()));
				Boolean ConsecuenciaExiste =  (procesoIndemnizacion.getInfoProducto().getConsecuencia().equals(proceso.getDocumento().getInfoProducto().getConsecuencia()));
				Boolean CoberturaExiste =  (procesoIndemnizacion.getInfoProducto().getCobertura().equals(proceso.getDocumento().getInfoProducto().getCobertura()));

//				log.info(FechaSiniestroExiste.toString());
//				log.info(NumeroPolizaExiste.toString());
//				log.info(CausaExiste.toString());
//				log.info(ConsecuenciaExiste.toString());
//				log.info(CoberturaExiste.toString());
				
				if (FechaSiniestroExiste && NumeroPolizaExiste && CausaExiste && ConsecuenciaExiste && CoberturaExiste) {
					CasoDuplicado=true;
					break;
				}
				
			}
			
			if (CasoDuplicado) {
				oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
				oResponseWS.setMensaje("Caso duplicado");
				log.error("Caso duplicado");
			}		

			
		}
		
		if(!CasoDuplicado) {	//Ingresa solo si no existe un caso creado igual (Caso Duplicado)
			try {

				Map<String, Object> variables = new HashMap<String, Object>();
				USUARIO_CREADOR = procesoIndemnizacion.getInfoProceso().getUsuarioCreador();
				variables.put("estadoCreacion", procesoIndemnizacion.getInfoProceso().getEstadoCreacion());
				variables.put("fechaCreacion", new Date());
				variables.put("canalCreacion", procesoIndemnizacion.getInfoProceso().getCanalCreacion());
				variables.put("clasificacionCaso", procesoIndemnizacion.getInfoProceso().getClasificacionCaso());
				variables.put("valorReserva", procesoIndemnizacion.getSiniestro().getValorReserva());
				variables.put("numeroDocumento", procesoIndemnizacion.getAsegurado().getNumeroDocumento());
				variables.put("ordenPagoBloqueada", false);	//Validar este dato de donde se obtiene
				variables.put("controlTecnico", false);		//Validar este dato de donde se obtiene				
				
				
				procesoIndemnizacion.getInfoProceso().setFechaCreacion(new Date());
				procesoIndemnizacion.getInfoProceso().setEstadoSolicitud(procesoIndemnizacion.getInfoProceso().getEstadoCreacion().getValor());
				
				if (procesoIndemnizacion.getInfoProceso().getClasificacionCaso().getValor().equals("Morado"))  {
					procesoIndemnizacion.getSiniestro().setFechaFormalizacion(new Date());
				}
				
//				variables.put("fechaSiniestro", procesoIndemnizacion.getSiniestro().getFechaSiniestro());				
//				variables.put("tipoDocumento", procesoIndemnizacion.getAsegurado().getTipoDocumento());
//				variables.put("numeroDocumento", procesoIndemnizacion.getAsegurado().getNumeroDocumento());
//				variables.put("nombres", procesoIndemnizacion.getAsegurado().getNombres());
//				variables.put("apellidos", procesoIndemnizacion.getAsegurado().getApellidos());
//				variables.put("compañia", procesoIndemnizacion.getInfoProducto().getCompania());
//				variables.put("ramoProducto", procesoIndemnizacion.getInfoProducto().getRamoProducto());
//				variables.put("producto", procesoIndemnizacion.getInfoProducto().getProducto());
//				variables.put("numeroPoliza", procesoIndemnizacion.getInfoProducto().getNumeroPoliza());				
//				variables.put("causa", procesoIndemnizacion.getInfoProducto().getCausa());
//				variables.put("consecuencia", procesoIndemnizacion.getInfoProducto().getConsecuencia());
//				variables.put("cobertura", procesoIndemnizacion.getInfoProducto().getCobertura());
//				variables.put("riesgo", procesoIndemnizacion.getInfoProducto().getRiesgo());
//				variables.put("resultadoScoreRiesgo", procesoIndemnizacion.getInfoProceso().getResultadoScoreRiesgo());
//				variables.put("resultadoMotorDefi", procesoIndemnizacion.getInfoProceso().getResultadoMotorDefi());
//				variables.put("documentos", procesoIndemnizacion.getInfoProceso().getDocumentos());
//				variables.put("usuarioCreador", USUARIO_CREADOR);
//				variables.put("CLV", procesoIndemnizacion.getAsegurado().getClv());
//				variables.put("numeroContacto", procesoIndemnizacion.getAsegurado().getNumeroContacto());
//				variables.put("email", procesoIndemnizacion.getAsegurado().getEmail());
//				variables.put("numeroSiniestro", procesoIndemnizacion.getSiniestro().getNumeroSiniestro());				
//				variables.put("valorPretension", procesoIndemnizacion.getSiniestro().getValorPretension());
				
				identityService.setAuthenticatedUserId(USUARIO_CREADOR);
				
				log.info("VariablesIn: " + variables);

				ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);
				identityService.setAuthenticatedUserId(null);
				log.info("VariablesIn2: " + variables);
				if (processInstance.getId() != null) {
					
					oConsecutivos.setIdFlowable(processInstance.getId());
					System.out.println("PROCESO:" + processInstance.getId().toString());
					ObjectMapper mapper = new ObjectMapper();
					try {
						log.info("procesoIndemnizacion: "+ mapper.writeValueAsString(procesoIndemnizacion));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					
					String consecutivoCreado =  infoProcesoRepository.P_CREAR_NUEVO_CASO(processInstance.getId().toString(), mapper.writeValueAsString(procesoIndemnizacion) );
					
					//String doc =  infoProcesoRepository.P_CREAR_NUEVO_CASO(processInstance.getId().toString(), mapper.writeValueAsString(procesoIndemnizacion) );
					
					//ProcesoIndemnizacion procesoIndemnizacion2  = new ObjectMapper().readValue(doc, ProcesoIndemnizacion.class);
					
					//String idConse = procesoIndemnizacion2.getInfoProceso().getIdConsecutivo();
					
//					InfoGeneralProceso obtenerProceso = infoProcesoRepository.findByIdProceso(processInstance.getId().toString()).get();
//					System.out.println("Poliza MAIN" + obtenerProceso.getDocumento().getInfoProducto().getNumeroPoliza());
					
					oConsecutivos.setIdConsecutivo(consecutivoCreado);

					
					if (consecutivoCreado != null) {
						runtimeService.setVariable(processInstance.getId(), "idConsecutivo", consecutivoCreado);
												
						System.out.println("Ingreso a Evaluar");
						variables.clear();
				        variables.put("idConsecutivo", consecutivoCreado);
				       
				        
				        new Thread(() -> {
				        	runtimeService.evaluateConditionalEvents(processInstance.getId(), variables);
				        }).start();
					    
				        
						oResponseWS.setResultado(oConsecutivos);
						oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
					}else {
						oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
						oResponseWS.setMensaje("Error generando consecutivo en base de datos");
					}
					
					
				}			

			} catch (Exception e) {

				System.err.println(e.getMessage());
				oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
				oResponseWS.setMensaje(e.getMessage() + "" + e.getClass());
				log.error("Error creando caso " +"|" + e.getMessage() + "|" + e.getClass());

			}
		}
			
		
		
		return oResponseWS;

	}

	@Override
	public List<Object> trazaHistoricoProceso(String processId) { // Listar los procesos sin finalizar

		List<Object> oProcesoList = new ArrayList<Object>();

		HistoryService historyService = processEngine.getHistoryService();

		List<HistoricProcessInstance> procesos = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processId)
				.includeProcessVariables()
				.unfinished()
				.orderByProcessInstanceStartTime()
				.asc()
				.list();

		for (HistoricProcessInstance proceso : procesos) {
			Procesos oProceso = new Procesos();

			String idCasoPropio = (String) proceso.getProcessVariables().get("idConsecutivo");

			oProceso.setIdProceso(proceso.getId());
			oProceso.setIdConsecutivo(idCasoPropio);
			oProceso.setFechaInicio(proceso.getStartTime());
			oProceso.setUsuarioCreador(proceso.getStartUserId());
			oProceso.setVariablesCaso(proceso.getProcessVariables());

			oProcesoList.add(oProceso);

			System.out.println("\n" + "IdProceso: " + proceso.getId() + " Variables: " + proceso.getProcessVariables()
					+ " FechaInicio: " + proceso.getStartTime() + " UsuarioCreador: " + proceso.getStartUserId());
		}

		System.out.println("\n \n \n \n");

		return oProcesoList;
	}

	


	

}