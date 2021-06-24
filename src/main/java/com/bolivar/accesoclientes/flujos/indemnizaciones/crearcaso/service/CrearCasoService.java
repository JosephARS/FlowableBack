package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.NuevoCaso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.Procesos;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.repository.ConsecutivosRepository;

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

	RuntimeService runtimeService;
	TaskService taskService;
	ProcessEngine processEngine;
	RepositoryService repositoryService;
	IdentityService identityService;
	ConsecutivosRepository consecutivosRepo;

	public void deployProcessDefinition() {

		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource("processes/IndemnizacionesGenerales.bpmn20.xml").deploy();
	}

	@Override
	public ResponseWS registrarNuevoCaso(NuevoCaso nuevoCaso) {

		ResponseWS oResponseWS = new ResponseWS();
		Consecutivos oConsecutivos = new Consecutivos();

		HistoryService historyService = processEngine.getHistoryService();		

		List<HistoricProcessInstance> procesos = historyService.createHistoricProcessInstanceQuery() 	// Se valida si ya existe un caso creado con los mismos datos.
				.variableValueEquals("numeroDocumento", nuevoCaso.getNumeroDocumento())
				.includeProcessVariables()
				.unfinished()
				.list();
		
		System.out.println("Procesos cantidad: " + procesos.size());
		//procesos.clear();
		Boolean CasoDuplicado = false;
		if (procesos.size() > 0) {
		
			for (HistoricProcessInstance proceso : procesos) {		//Compara que los casos encontrados sean diferentes alcaso que se va a crear.
				
				Boolean FechaSiniestroExiste = (nuevoCaso.getFechaSiniestro().equals(proceso.getProcessVariables().get("fechaSiniestro")));
				Boolean NumeroPolizaExiste =  (nuevoCaso.getNumeroPoliza().equals(proceso.getProcessVariables().get("numeroPoliza")));
				Boolean CausaExiste =  (nuevoCaso.getCausa().equals(proceso.getProcessVariables().get("causa")));
				Boolean ConsecuenciaExiste =  (nuevoCaso.getConsecuencia().equals(proceso.getProcessVariables().get("consecuencia")));
				Boolean CoberturaExiste =  (nuevoCaso.getCobertura().equals(proceso.getProcessVariables().get("cobertura")));

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
				variables.put("estadoCaso", nuevoCaso.getEstadoCaso());
				variables.put("fechaCreacion", new Date());
				variables.put("fechaSiniestro", nuevoCaso.getFechaSiniestro());
				variables.put("canalCreacion", nuevoCaso.getCanalCreacion());
				variables.put("tipoDocumento", nuevoCaso.getTipoDocumento());
				variables.put("numeroDocumento", nuevoCaso.getNumeroDocumento());
				variables.put("nombres", nuevoCaso.getNombres());
				variables.put("apellidos", nuevoCaso.getApellidos());
				variables.put("ramoProducto", nuevoCaso.getRamoProducto());
				variables.put("producto", nuevoCaso.getProducto());
				variables.put("numeroPoliza", nuevoCaso.getNumeroPoliza());
				variables.put("clasificacionCaso", nuevoCaso.getClasificacionCaso());
				variables.put("causa", nuevoCaso.getCausa());
				variables.put("consecuencia", nuevoCaso.getConsecuencia());
				variables.put("cobertura", nuevoCaso.getCobertura());
				variables.put("riesgo", nuevoCaso.getRiesgo());
				variables.put("resultadoScoreRiesgo", nuevoCaso.getResultadoScoreRiesgo());
				variables.put("resultadoMotorClasifica", nuevoCaso.getResultadoMotorClasifica());
				variables.put("resultadoMotorDefi", nuevoCaso.getResultadoMotorDefi());
				variables.put("documentos", nuevoCaso.getDocumentos());
				variables.put("usuarioCreador", nuevoCaso.getUsuarioCreador());

				log.info("VariablesIn: " + variables);

				ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);
				log.info("VariablesIn2: " + variables);
				if (processInstance.getId() != null) {
					
					oConsecutivos.setIdFlowable(processInstance.getId());
					System.out.println("PROCESO:" + processInstance.getId().toString());
					String idConse = consecutivosRepo.P_CREAR_CONSECUTIVO(processInstance.getId().toString());
					oConsecutivos.setIdConsecutivo(idConse);
					System.out.println("CCCCCC:" + idConse);
					
					if (idConse != null) {
						//runtimeService.setVariable(processInstance.getId(), "idConsecutivo", idConse);
						
						System.out.println("Ingreso a Evaluar");
						variables.clear();
				        variables.put("idConsecutivo", idConse);
				        runtimeService.evaluateConditionalEvents(processInstance.getId(), variables);
					    
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
			oProceso.setIdCasoPropio(idCasoPropio);
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