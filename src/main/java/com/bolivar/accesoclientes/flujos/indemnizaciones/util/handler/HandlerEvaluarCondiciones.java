package com.bolivar.accesoclientes.flujos.indemnizaciones.util.handler;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;

import lombok.extern.slf4j.Slf4j;

//@Component
@Slf4j
public class HandlerEvaluarCondiciones implements JavaDelegate{
	
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	InfoGeneralProcesoRepository infoProcesoRepository;
	
	HistoryService historyService = processEngine.getHistoryService();
	
	//@Bean
	public Boolean ResultadoMotorDefi() {
		
//		HistoricProcessInstance proceso = historyService.createHistoricProcessInstanceQuery()
//				.variableValueEquals("idConsecutivo", Idconsecutivo).includeProcessVariables().singleResult();
//		
//		String idProceso = proceso.getId();
//		log.info(idProceso);
//		
//		InfoGeneralProceso infoGeneralProceso = infoProcesoRepository.findByIdProceso(idProceso).get();
//		
//		String resultadoMotor = infoGeneralProceso.getDocumento().getInfoProceso().getResultadoMotorDefi().getValor();
//		String cambioResultadoMotor = infoGeneralProceso.getDocumento().getInfoProceso().getCambioMotorDef().getValor();
//		log.info("resultadoMotor: " + resultadoMotor);
//		log.info("cambioResultadoMotor: " + cambioResultadoMotor);
//		if (cambioResultadoMotor == null || cambioResultadoMotor.isBlank()) {
//			log.info("resultadoMotor: " + resultadoMotor);
//			return resultadoMotor.equals(valor);
//		} else {
//			log.info("cambioResultadoMotor: " + cambioResultadoMotor);
//			return cambioResultadoMotor.equals(valor);
//		}
//		
		
		log.info("cambioResultadoMotor: " );
		return true;
	}

	@Override
	public void execute(DelegateExecution execution) {
		log.info("cambioResultadoMotor2: " );
		
	}

	
	
}
