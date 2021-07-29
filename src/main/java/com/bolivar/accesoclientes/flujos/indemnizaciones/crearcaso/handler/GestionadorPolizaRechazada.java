package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.handler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

@Data
@Slf4j
public class GestionadorPolizaRechazada implements JavaDelegate{
	 
	@Override
	    public void execute(DelegateExecution execution) {

//		try {
//			System.out.println("EMpieza Timer 10 Seg");
//			TimeUnit.SECONDS.sleep(10);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		log.info("Se ha enviado correo al cliente");
		}
}
