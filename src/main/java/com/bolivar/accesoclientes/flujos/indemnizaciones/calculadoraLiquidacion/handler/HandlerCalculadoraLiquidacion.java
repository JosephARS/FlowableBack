package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.handler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model.ProcesoCalc;
import com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model.RequestCalculadoraDTO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.model.ResponseCalculadoraDTO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.NuevoCaso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.EstadoSolicitud;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.InfoGeneralProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HandlerCalculadoraLiquidacion implements JavaDelegate {
	
	@Autowired
	private Environment env;
	
	@Autowired
	InfoGeneralProcesoRepository infoProcesoRepository;

	
	@Override
	public void execute(DelegateExecution execution) {
		
//		System.out.println("exe:"+ execution.getRootProcessInstanceId());
//		System.out.println("exe:"+ execution.getProcessInstanceId());
//		Optional<InfoGeneralProceso> obtenerProceso = Optional.ofNullable(infoProcesoRepository.findByIdProceso(execution.getProcessInstanceId().toString()).get());
//		System.out.println(obtenerProceso.get().getDocumento().getInfoProducto().getNumeroPoliza());
//		
//		List<String> conteoProcesosActivos = new ArrayList<String>();
//		conteoProcesosActivos.add("8d493f67-ea46-11eb-9cbd-b07d648ec81d");
		
		String letraString = "a";
		System.out.println(letraString);
		
		
		//List<InfoGeneralProceso> validarProcesoRepetido = infoProcesoRepository.findByIdProcesoIn(conteoProcesosActivos);
//		System.out.println(conteoProcesosActivos);
		
		
		
		try {

			RestTemplate restTemplate = new RestTemplate();
			String idProceso = execution.getRootProcessInstanceId();
			
//			String idProceso = execution.getRootProcessInstanceId();
			
//			System.out.println("Poliza" + obtenerProceso.getDocumento().getInfoProducto().getNumeroPoliza());
			
			Optional<InfoGeneralProceso> obtenerProceso = infoProcesoRepository.findByIdProceso(idProceso);
			
			//System.out.println("Poliza" + obtenerProceso.getDocumento().getInfoProducto().getNumeroPoliza());
			
			String CALCULADORA_LIQUIDACION = "spring.profiles.calculadoraLiquidacion";

			//NuevoCaso variables = (NuevoCaso) execution.getVariables();
			SimpleDateFormat fecha = new SimpleDateFormat("mm-dd-yyyy"); 
			long codCia = 3;
			long codSecc = 23;
			long codRamo = 117;
			Long numPol1 = (Long) obtenerProceso.get().getDocumento().getInfoProducto().getNumeroPoliza();
			long codRies = 1;
			Date fechaSini = null;
			try {
				fechaSini = fecha.parse("06-01-2019");
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
			System.out.println(fechaSini);

			//String urlString = "https://fz73xehwah.execute-api.us-east-1.amazonaws.com/dev/poliza/api/v1/siniestros/calculadora";

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
			try {
				log.info("Calculadora: "+ mapper.writeValueAsString(requestCalculadora));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HttpEntity<RequestCalculadoraDTO> httpEntity = new HttpEntity<>(requestCalculadora, headers);
			ResponseCalculadoraDTO result = restTemplate.postForObject(urlString, httpEntity, ResponseCalculadoraDTO.class);
			
			if (result.getCodRespuesta() < 1) {
				log.info(result.getCodRespuesta().toString());
			}
			

			log.info("Consulta de calculadora liquidacion: " + result);
			
			if (result.getValIndemnizar() != null) {
				System.out.println(result.getValIndemnizar());
				int res = infoProcesoRepository.updateValorByIdProceso(idProceso, result.getValIndemnizar());
				int respuesta2 = infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.PAGO);
				System.out.println(res); 
			}

			
		} catch (Exception e) {
			log.error("Error en calculadora liquidacion " + " | " + e.getMessage() + " | " + e.getClass() + " | "
					+ e.getLocalizedMessage() );

		} 
	}

}
