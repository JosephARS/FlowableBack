package com.bolivar.accesoclientes.flujos.indemnizaciones.actualizarValorReserva.service;

import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.VariablesProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ActualizarValorReservaService {
	
	@Autowired
	InfoGeneralProcesoRepository infoProcesoRepository;

	public boolean consultaServicioSimon(VariablesProceso variablesProceso) {
		
		
		Long respuesta = (long) 100000;
				
		Long valorActual = variablesProceso.getSiniestro().getValorReserva();
		log.info(respuesta.toString(), valorActual.toString());
		
		if (!respuesta.equals(valorActual)) {
			
			String idProceso = variablesProceso.getInfoProceso().getIdProceso();
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				log.info("procesoIndemnizacion: "+ mapper.writeValueAsString(variablesProceso.getSiniestro()));
				infoProcesoRepository.actualizarValorReserva(idProceso, mapper.writeValueAsString(variablesProceso.getSiniestro()));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e.getCause());
				e.printStackTrace();
			}	
			
			
			//infoProcesoRepository.updateValorReserva(variablesProceso.getInfoProceso().getIdProceso(), respuesta);
			//System.out.println(infoProcesoRepository.updateValorReserva(idProceso, respuesta, tipoEvento));
			
			//System.out.println(infoProcesoRepository.actualizarValorReserva(idProceso, respuesta, tipoEvento));
			
			
			return true;
		}else {
			return false;
		}
		
		
		
		
		
		
	}
	
}
