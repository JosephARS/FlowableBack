package com.bolivar.accesoclientes.flujos.indemnizaciones.util.handler;

import java.util.Calendar;
import java.util.Date;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HandlerCalcularFechaVencimiento implements TaskListener{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegateTask) {
				
		Integer horasAtencionTarea = 48;
		Date date = null;
						
		Calendar fechaVencimiento = Calendar.getInstance();
		fechaVencimiento.add(Calendar.HOUR_OF_DAY, horasAtencionTarea);
		
		while (fechaVencimiento.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || fechaVencimiento.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			fechaVencimiento.add(Calendar.HOUR_OF_DAY, 24);
			System.out.println(fechaVencimiento);

			
		}
		date = fechaVencimiento.getTime();
		System.out.println(date);
		
		delegateTask.setDueDate(date);
		
		log.info("Fecha de vencimiento: " + date);
	}
	

}
