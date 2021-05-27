package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.handler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

@Data
@Slf4j
public class GestionadorPolizaRechazada implements JavaDelegate{
	 
	@Override
	    public void execute(DelegateExecution execution) {

		log.info("Se ha enviado correo al cliente");
		}
}
