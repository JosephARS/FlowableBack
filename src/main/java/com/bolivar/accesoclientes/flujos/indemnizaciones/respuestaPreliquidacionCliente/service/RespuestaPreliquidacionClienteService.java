package com.bolivar.accesoclientes.flujos.indemnizaciones.respuestaPreliquidacionCliente.service;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;
import com.bolivar.accesoclientes.flujos.indemnizaciones.respuestaPreliquidacionCliente.DAO.RespuestaPreliquidacionClienteDAO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RespuestaPreliquidacionClienteService implements RespuestaPreliquidacionClienteDAO{
	
	RuntimeService runtimeService;


	@Override
	public ResponseWS mensajePreliquidacion(String idConsecutivo, Boolean clienteAcepta) {
		
		ResponseWS oResponseWS = new ResponseWS();
		
		Map<String, Object> variables = new HashMap<String, Object>();

		try {
			Execution execution = runtimeService.createExecutionQuery()
				      .messageEventSubscriptionName("Preliquidacion revisada")
				      .processVariableValueEquals("idConsecutivo", idConsecutivo)
				      .singleResult();
			
			String idExecution = execution.getId();
			
			System.out.println(idExecution + " / " + idConsecutivo);
			
			String mensaje = (clienteAcepta) ? "Aceptada": "Rechazada";		
			variables.put("respuestaClientePreliquidacion", mensaje);
			log.info(variables.toString());
			
			runtimeService.messageEventReceived("Preliquidacion revisada", idExecution, variables);
			
			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
			oResponseWS.setResultado(variables);
		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Consecutivo no se encuentra o no tiene preliquidacion pendiente " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
			log.error("Consecutivo no se encuentra o no tiene preliquidacion pendiente " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
		}
		

		
		return oResponseWS;
	}

}
