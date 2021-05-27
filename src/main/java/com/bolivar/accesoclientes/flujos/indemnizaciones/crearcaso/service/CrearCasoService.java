package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.DAO.CrearCasoDAO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.Consecutivos;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.NuevoCaso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.Procesos;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.RespuestaInstanciaProcesoDto;
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
	ConsecutivosRepository consecutivosRepo;

	public void deployProcessDefinition() {

		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource("processes/IndemnizacionesGenerales.bpmn20.xml").deploy();
	}
	
	@Override
	public RespuestaInstanciaProcesoDto registrarNuevoCaso(NuevoCaso nuevoCaso) {
		
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("estadoCaso", nuevoCaso.getEstadoCaso());
        variables.put("fechaCreacion", nuevoCaso.getFechaCreacion());
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
        variables.put("documentos", nuevoCaso.getDocumentos());
        log.info("VariablesIn: " + variables);
        //System.out.println("VariablesIn: " + variables);
        
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);       

        
        Consecutivos oConsecutivos = new Consecutivos();
        oConsecutivos.setIdFlowable(processInstance.getId());
        System.out.println("PROCESO:" + processInstance.getId().toString());
        String idConse = consecutivosRepo.P_CREAR_CONSECUTIVO(processInstance.getId().toString());
        
        runtimeService.setVariable(processInstance.getId(),"idConsecutivo", idConse );
        
        
        return new RespuestaInstanciaProcesoDto(processInstance.getId(), processInstance.isEnded(), idConse);
		
	}
	
	@Override
	public List<Object> trazaHistoricoProceso(String processId) {		//Listar los procesos sin finalizar    	
    	
    	List<Object> oProcesoList = new ArrayList<Object>();
    	
        HistoryService historyService = processEngine.getHistoryService();

        List<HistoricProcessInstance> procesos =
                historyService
                        .createHistoricProcessInstanceQuery()
                        .processInstanceId(processId)
                        .includeProcessVariables()
                        .unfinished()                        
                        .orderByProcessInstanceStartTime()
                        .asc()
                        .list();

        
        for (HistoricProcessInstance proceso : procesos) {
        	Procesos oProceso =new Procesos() ;
        	
        	String idCasoPropio = (String) proceso.getProcessVariables().get("idCasoPropio");
        	
            oProceso.setIdProceso(proceso.getId());
            oProceso.setIdCasoPropio(idCasoPropio);
            oProceso.setFechaInicio(proceso.getStartTime());
            oProceso.setUsuarioCreador(proceso.getStartUserId());
        	
            oProcesoList.add(oProceso);
            
            System.out.println("\n" +
                    "IdProceso: " + proceso.getId() +  " Variables: " + proceso.getProcessVariables() + " FechaInicio: " + proceso.getStartTime() + " UsuarioCreador: " + proceso.getStartUserId());
        }

        System.out.println("\n \n \n \n");
        
        return oProcesoList;
    }
	
	
}