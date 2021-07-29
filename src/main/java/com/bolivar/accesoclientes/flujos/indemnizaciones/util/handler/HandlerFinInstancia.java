package com.bolivar.accesoclientes.flujos.indemnizaciones.util.handler;

import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HandlerFinInstancia implements FlowableEventListener{
    
	RuntimeService runtimeService;

	@Override
	public void onEvent(FlowableEvent event) {
		
		log.info("Se ha anulado/cancelado una Instancia " + event.getType().name());
	}
	
	@Override
	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isFireOnTransactionLifecycleEvent() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getOnTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

}
