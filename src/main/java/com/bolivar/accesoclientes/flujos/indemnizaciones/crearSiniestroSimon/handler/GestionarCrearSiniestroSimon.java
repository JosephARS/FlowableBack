package com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.handler;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class GestionarCrearSiniestroSimon implements JavaDelegate{
	 
	@Override
	    public void execute(DelegateExecution execution) {

		log.info("Se ha creado Siniestro en Simon");
		}

}
