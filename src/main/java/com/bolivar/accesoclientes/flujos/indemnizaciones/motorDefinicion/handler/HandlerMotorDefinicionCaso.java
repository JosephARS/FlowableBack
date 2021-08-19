package com.bolivar.accesoclientes.flujos.indemnizaciones.motorDefinicion.handler;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.InfoProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.ObjCodigoValor;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class HandlerMotorDefinicionCaso implements JavaDelegate{
	
	@Autowired
	InfoGeneralProcesoRepository infoProcesoRepository;
	 
	@Override
	    public void execute(DelegateExecution execution) {

		String idProceso = execution.getProcessInstanceId();
		
		ObjCodigoValor resultado = new ObjCodigoValor();
		resultado.setCodigo("00");
		resultado.setValor("Otra Linea");
		
		
		execution.setVariable("resultadoMotorDefi", resultado);
		
		InfoProceso infoProceso = new InfoProceso();
		
		infoProceso.setResultadoMotorDefi(resultado);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			log.info("procesoIndemnizacion: "+ mapper.writeValueAsString(infoProceso));
			infoProcesoRepository.P_ACTUALIZAR_MOTOR_DEF(idProceso, mapper.writeValueAsString(infoProceso));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}	
		
		log.info("Se ha consultado el Motor de definicion");
		
		}

}
