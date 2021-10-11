package com.bolivar.accesoclientes.flujos.indemnizaciones.actualizarValorReserva.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.bolivar.accesoclientes.flujos.indemnizaciones.actualizarValorReserva.model.ResponseConsultaReserva;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.model.RequestCrearSiniestro;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.model.ResponseCrearSiniestro;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.EstadoSolicitud;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.InfoGeneralProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.VariablesProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

//@Service
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@AllArgsConstructor(onConstructor = @__(@Autowired))
@Component
@Slf4j
public class ActualizarValorReservaService {
	
	@Autowired
	InfoGeneralProcesoRepository infoProcesoRepository;
	
	@Autowired
	private Environment env;

	public long consultaServicioSimon(VariablesProceso variablesProceso) {
		
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		
		String idProceso = variablesProceso.getInfoProceso().getIdProceso();
		Long numeroSiniestro = variablesProceso.getSiniestro().getNumeroSiniestro();
		
		long valorRva = 0;
		
		String CONSULTA_VALOR_RESERVA = "spring.profiles.consultaValorReserva";
		String urlString = env.getProperty(CONSULTA_VALOR_RESERVA);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("x-api-key", "4Qsus5DlSD8gXesXfEBzN4ZBRrXaw67e4IQ6CFZX");
		headers.add("codCia", "3");
		headers.add("codSecc", "1");
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlString)
		        .queryParam("NumSini", numeroSiniestro);
		
		try {
			HttpEntity<?> httpEntity = new HttpEntity<>(headers);
			ResponseEntity<ResponseConsultaReserva> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, ResponseConsultaReserva.class);
			
			if (result != null) {
					
					valorRva = result.getBody().getPrvaTotal();
					variablesProceso.getSiniestro().setValorReserva(valorRva);
					log.info("Valor de la reserva:" + valorRva);
					int res = infoProcesoRepository.actualizarValorReserva(idProceso, mapper.writeValueAsString(variablesProceso.getSiniestro()));


			}
			
		} catch (Exception e) {
			log.error("ErrorWS :: en Actulizar valor de reserva: " + e.getMessage() + " | " + e.getClass() + " | " + e.getStackTrace()[0] );
			valorRva = 0;
		}

		return valorRva;
		
	}
	
}
