package com.bolivar.accesoclientes.flujos.indemnizaciones.generarPago.handler;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HandlerGenerarOrdenPago implements JavaDelegate {
	
	@Autowired
	private Environment env;
	
	@Autowired
	InfoGeneralProcesoRepository infoProcesoRepository;
	
	
	@Override
	public void execute(DelegateExecution execution) {
		log.info("Se ha generado la Orden de pago");
	}

}
