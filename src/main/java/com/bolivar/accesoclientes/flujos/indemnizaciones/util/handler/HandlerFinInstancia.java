package com.bolivar.accesoclientes.flujos.indemnizaciones.util.handler;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HandlerFinInstancia implements ExecutionListener{
    
	
	@Autowired
	InfoGeneralProcesoRepository infoProcesoRepository;
	
	RuntimeService runtimeService;

	@Override
	public void notify(DelegateExecution execution) {

		String idProceso = execution.getProcessInstanceId();

		
		infoProcesoRepository.P_FINALIZAR_PROCESO(idProceso, "");
		
		log.info("Se ha finalizado una Instancia " + execution.getProcessInstanceId() + execution.getEventName() + execution.getCurrentActivityId());
	}
	


}
