package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.ManagementService;
import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.job.api.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model.ProcesoCalc;
import com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model.RequestCalculadoraDTO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model.ResponseCalculadoraDTO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.EstadoSolicitud;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.InfoGeneralProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HandlerCalculadoraLiquidacion implements JavaDelegate {
	
	@Autowired
	ManagementService managementService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	InfoGeneralProcesoRepository infoProcesoRepository;

	
	@Override
	@Retryable(value = BpmnError.class, maxAttempts = 2, backoff = @Backoff(1000))
	public void execute(DelegateExecution execution){ 
		
		RestTemplate restTemplate = new RestTemplate();
		String CALCULADORA_LIQUIDACION = "spring.profiles.calculadoraLiquidacion";
		String idProceso = execution.getRootProcessInstanceId();
		
		try {
			
			Optional<InfoGeneralProceso> obtenerProceso = infoProcesoRepository.findByIdProceso(idProceso);

			SimpleDateFormat fecha = new SimpleDateFormat("mm-dd-yyyy"); 
			long codCia = 3;
			long codSecc = 23;
			long codRamo = 117;
			Long numPol1 = null;
			if (obtenerProceso.isPresent()) {
				numPol1 =  obtenerProceso.get().getDocumento().getInfoProducto().getNumeroPoliza();
			}
			long codRies = 1;
			Date fechaSini = null;

			fechaSini = fecha.parse("06-01-2019");

			long codCausa = 129;
			long codCons = 74;
			long codCob = 351;
			long valPretension = 7500000;
			String validacion = "S";
			ProcesoCalc proceso = new ProcesoCalc();
			proceso.setModulo("7");
			proceso.setProceso(70);
			proceso.setSubProceso(null);
			proceso.setProceso(3);
			System.out.println(execution.getId());
			System.out.println(execution.getProcessInstanceId());

			String urlString = env.getProperty(CALCULADORA_LIQUIDACION);
			System.out.println("URL:" + urlString);

			HttpHeaders headers = new HttpHeaders();
			headers.add("x-api-key", "4Qsus5DlSD8gXesXfEBzN4ZBRrXaw67e4IQ6CFZX");
			headers.add("modulo", "7");
			headers.add("proceso", "70");
			headers.add("codcia", "3");
			

			RequestCalculadoraDTO requestCalculadora = new RequestCalculadoraDTO();
			requestCalculadora.setCodCia(codCia);
			requestCalculadora.setCodSecc(codSecc);
			requestCalculadora.setCodRamo(codRamo);
			requestCalculadora.setNumPol1(numPol1);
			requestCalculadora.setCodRies(codRies);
			requestCalculadora.setFechaSini(fechaSini);
			requestCalculadora.setCodCausa(codCausa);
			requestCalculadora.setCodCons(codCons);
			requestCalculadora.setCodCob(codCob);
			requestCalculadora.setValPretension(valPretension);
			requestCalculadora.setValidacion(validacion);
			requestCalculadora.setProceso(proceso);
			
			ObjectMapper mapper = new ObjectMapper();

			log.info("Calculadora: "+ mapper.writeValueAsString(requestCalculadora));

			

				HttpEntity<RequestCalculadoraDTO> httpEntity = new HttpEntity<>(requestCalculadora, headers);
				ResponseEntity<Object> result = restTemplate.exchange(urlString, HttpMethod.POST, httpEntity, Object.class);
					
				ResponseCalculadoraDTO oResponseCalc = (ResponseCalculadoraDTO) result.getBody();
				
				log.info("Codigo respuesta: " + result.getBody());
				log.info("Consulta de calculadora liquidacion: " + oResponseCalc);
				
				int res = infoProcesoRepository.updateValorByIdProceso(idProceso, oResponseCalc.getValIndemnizar());
				int respuesta2 = infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.PAGO);
				System.out.println(res);

			
			} catch (HttpStatusCodeException e) {

				boolean a = true; //001			
				
				log.error("ErrorWS :: respuesta servicio Calculadora liquidacion: " + e.getStatusCode() + " | " + e.getResponseBodyAsString());
							
				if (e.getStatusCode().is4xxClientError() && a) {

					List<Job> job = managementService.createJobQuery().executionId(execution.getId()).list();

					job.forEach(x -> {
				    	log.info("Job reintentos: {}", x.getRetries());
				    	log.info("Job actividad: {}", x.getElementName());
				    	log.info("Job mensaje excepcion: {}", x.getExceptionMessage());
					});
					infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.ERROR_SW);
					throw new FlowableException(e.getMessage());	//Error lanzado para que el proceso quede suspendido en "Dead letter".
				}
				else {
					throw new BpmnError("002",e.getMessage()); //Error lanzado para que flowable reintente mas tarde
				}


			}  catch (Exception e) {
				log.error("Error en calculadora liquidacion " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getStackTrace()[0] );
				infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.ERROR_SW);
				throw new FlowableException(e.getMessage());
			} 

			
		
	}


}
