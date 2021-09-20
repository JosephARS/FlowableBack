package com.bolivar.accesoclientes.flujos.indemnizaciones.util.handler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Tarea;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.TareaRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HandlerCalcularFechaVencimiento implements TaskListener{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	TareaRepository tareaRepository;

	@Override
	public void notify(DelegateTask delegateTask) {
		
		String tareaDef =  delegateTask.getTaskDefinitionKey();
		log.info(tareaDef);
		
		
		List<Tarea> tareaList = tareaRepository.P_OBTENER_TIEMPO_ATENCION();
		
		log.info("Tarea: " + tareaList);
		
		Tarea oTarea =  tareaList.stream().filter(x -> x.getIdTareaDefinicion().equals(tareaDef)).findFirst().get();
			
		Integer horasAtencionTarea = Integer.parseInt(oTarea.getTiempoAtencion());
		
		if (horasAtencionTarea > 0) {
			
			Calendar fechaVencimiento = Calendar.getInstance();
			fechaVencimiento.add(Calendar.HOUR_OF_DAY, horasAtencionTarea);
			
			while (fechaVencimiento.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || fechaVencimiento.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				fechaVencimiento.add(Calendar.HOUR_OF_DAY, 24);
				System.out.println(fechaVencimiento);			
			}
			Date date = fechaVencimiento.getTime();
			System.out.println(date);
			
			delegateTask.setDueDate(date);
			
			log.info("Fecha de vencimiento: " + date);
		}
						

	}
	

}
