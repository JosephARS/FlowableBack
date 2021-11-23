package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

import com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model.CoberturaCalc;
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

			String fechaSini = fecha.format(obtenerProceso.get().getDocumento().getSiniestro().getFechaSiniestro());

			ProcesoCalc proceso = new ProcesoCalc();
			proceso.setModulo("7");
			proceso.setProceso(70);
			
			List<CoberturaCalc> coberturasList = new ArrayList<CoberturaCalc>();
			
			obtenerProceso.get().getDocumento().getInfoProducto().getCobertura().forEach(coberturaItem -> {
				CoberturaCalc oCoberturaCalc = new CoberturaCalc();
				oCoberturaCalc.setCodCobSini(Long.valueOf(coberturaItem.getCoberturaSini().getCodigo()));
				oCoberturaCalc.setValorPretension(Long.valueOf(coberturaItem.getValorPretensionCob()));
				oCoberturaCalc.setModeloRiesgoPretension(Long.valueOf(coberturaItem.getScoreRiesgoCob().getCodigo()));
				oCoberturaCalc.setClasMotorCaso(Long.valueOf(coberturaItem.getClasMotorCasoCob().getCodigo()));
				oCoberturaCalc.setMotorDefini(Long.valueOf(coberturaItem.getMotorDefiniCob().getCodigo()));
				coberturasList.add(oCoberturaCalc);
			});

			System.out.println(execution.getProcessInstanceId());

			String urlString = env.getProperty(CALCULADORA_LIQUIDACION);
			System.out.println("URL:" + urlString);

			HttpHeaders headers = new HttpHeaders();
			headers.add("x-api-key", "4Qsus5DlSD8gXesXfEBzN4ZBRrXaw67e4IQ6CFZX");			

			RequestCalculadoraDTO requestCalculadora = new RequestCalculadoraDTO();
			requestCalculadora.setCompania(3);
			requestCalculadora.setSeccion(23);
			requestCalculadora.setProducto(117);
			requestCalculadora.setCodUser("Flowable");
			requestCalculadora.setNumPol(obtenerProceso.get().getDocumento().getInfoProducto().getNumeroPoliza().toString());
			requestCalculadora.setNumSecuPol(obtenerProceso.get().getDocumento().getInfoProducto().getNumSecuPoliza().toString());
			requestCalculadora.setNumEnd(obtenerProceso.get().getDocumento().getInfoProducto().getNumEndoso());
			requestCalculadora.setCodRies(Long.valueOf(obtenerProceso.get().getDocumento().getInfoProducto().getRiesgo().getCodigo()));
			requestCalculadora.setTdocTercero(obtenerProceso.get().getDocumento().getAsegurado().getTipoDocumento());
			requestCalculadora.setCodAseg(obtenerProceso.get().getDocumento().getAsegurado().getNumeroDocumento());
			requestCalculadora.setNomAseg(obtenerProceso.get().getDocumento().getAsegurado().getNombres());
			requestCalculadora.setApeAseg(obtenerProceso.get().getDocumento().getAsegurado().getApellidos());
			requestCalculadora.setFechaSini(fechaSini);
			requestCalculadora.setHoraSini(obtenerProceso.get().getDocumento().getSiniestro().getHoraSiniestro());
			requestCalculadora.setDescSini("Número de caso: " + obtenerProceso.get().getDocumento().getInfoProceso().getIdConsecutivo());
			requestCalculadora.setCodCausaSini(Long.valueOf(obtenerProceso.get().getDocumento().getInfoProducto().getCausa().getCodigo()));
			requestCalculadora.setClv(Long.valueOf(obtenerProceso.get().getDocumento().getAsegurado().getClv().getCodigo()));
			requestCalculadora.setModeloRiesgo(Long.valueOf(obtenerProceso.get().getDocumento().getInfoProceso().getResultadoScoreRiesgo().getCodigo()));
			requestCalculadora.setValorEncuesta(Long.valueOf(0));
			requestCalculadora.setCodConsSini(Long.valueOf(obtenerProceso.get().getDocumento().getInfoProducto().getCobertura().get(0).getConsecuenciaCob().getCodigo()));
			requestCalculadora.setValorEvidencia(Long.valueOf(0));

			requestCalculadora.setCobertura(coberturasList);
			requestCalculadora.setValidacion("S");
			requestCalculadora.setProceso(proceso);

			
			ObjectMapper mapper = new ObjectMapper();

			log.info("Calculadora: "+ mapper.writeValueAsString(requestCalculadora));

			

			HttpEntity<RequestCalculadoraDTO> httpEntity = new HttpEntity<>(requestCalculadora, headers);
			ResponseEntity<Object> result = restTemplate.exchange(urlString, HttpMethod.POST, httpEntity, Object.class);
				
			ResponseCalculadoraDTO oResponseCalc = (ResponseCalculadoraDTO) result.getBody();
			
			log.info("Codigo respuesta: " + result.getBody());
			log.info("Consulta de calculadora liquidacion: " + oResponseCalc);
			
			int res = infoProcesoRepository.updateValorByIdProceso(idProceso, oResponseCalc.getValorIndemnizar());
			int respuesta2 = infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.PAGO);
			System.out.println(res + respuesta2);

			
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
//					throw new FlowableException(e.getMessage());	//Error lanzado para que el proceso quede suspendido en "Dead letter".
				}
				else {
//					throw new BpmnError("002",e.getMessage()); //Error lanzado para que flowable reintente mas tarde
				}


			}  catch (Exception e) {
				log.error("Error en calculadora liquidacion " + " | " + e.getMessage() + " | " + e.getClass() + " | " + e.getStackTrace()[0] );
				infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.ERROR_SW);
//				throw new FlowableException(e.getMessage());
			} 

			
		
	}


}
