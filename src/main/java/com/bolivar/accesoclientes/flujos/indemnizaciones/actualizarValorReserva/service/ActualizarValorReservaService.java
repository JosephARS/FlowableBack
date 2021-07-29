package com.bolivar.accesoclientes.flujos.indemnizaciones.actualizarValorReserva.service;

import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.VariablesProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;

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

	public boolean consultaServicioSimon(VariablesProceso variablesProceso, String tipoEvento) {
		
		
		Long respuesta = (long) 100000;
				
		Long valorActual = variablesProceso.getSiniestro().getValorReserva();
		log.info(respuesta.toString(), valorActual.toString());
		
		if (respuesta != valorActual) {
			//infoProcesoRepository.updateValorReserva(variablesProceso.getInfoProceso().getIdProceso(), respuesta);
			System.out.println(infoProcesoRepository.updateValorReserva(variablesProceso.getInfoProceso().getIdProceso(), respuesta, tipoEvento));
			return true;
		}else {
			return false;
		}
		
		
		
		
		
		
	}
	
}
