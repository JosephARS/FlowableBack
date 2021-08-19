package com.bolivar.accesoclientes.flujos.indemnizaciones.solicitarInfoAdicional.handler;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HandlerAccesoClienteInfoAdicional implements JavaDelegate{
	 
	@Override
	    public void execute(DelegateExecution execution) {
			
		log.info("Se ha solicitado a acceso cliente informacion adicional");
		}

}
