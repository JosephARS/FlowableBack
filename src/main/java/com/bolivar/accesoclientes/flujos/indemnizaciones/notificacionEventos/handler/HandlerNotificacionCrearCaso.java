package com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model.Dato;
import com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model.Grupo;
import com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model.RequestEventosDTO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model.ResponseEventosDTO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.InfoGeneralProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HandlerNotificacionCrearCaso implements JavaDelegate {

	@Autowired
	private Environment env;
	
	@Autowired
	InfoGeneralProcesoRepository infoProcesoRepository;

	@Override
	public void execute(DelegateExecution execution) {

		try {

			RestTemplate restTemplate = new RestTemplate();
			String idProceso = execution.getRootProcessInstanceId();
			
			Optional<InfoGeneralProceso> oInfoGeneralProceso = infoProcesoRepository.findByIdProceso(idProceso);
			log.info("eventos: " + oInfoGeneralProceso.get());
			String ramo = oInfoGeneralProceso.get().getDocumento().getInfoProducto().getRamo().getValor();
			
			String NOTIFICACION_EVENTOS = "spring.profiles.notificacionEventos";
			String APLICACION = "servicios.notificacionEventos.crearCaso.aplicacion";
			String EVENTO = "servicios.notificacionEventos.crearCaso.evento";
			String API_KEY = "servicios.notificacionEventos.apiKey";

			String idConsecutivo = (String) execution.getVariable("idConsecutivo");

			String urlString = env.getProperty(NOTIFICACION_EVENTOS);
			
			System.out.println("URL:" + urlString);

			HttpHeaders headers = new HttpHeaders();
			headers.add("x-api-key", env.getProperty(API_KEY));

			RequestEventosDTO request = new RequestEventosDTO();
			request.setAplicacion(env.getProperty(APLICACION));
			request.setEvento(env.getProperty(EVENTO));

			List<Object> datos = Arrays.asList(new Dato("P_PRODUCTO", ramo), new Dato("P_RADICADO", idConsecutivo));
							

			Grupo grupo0 = new Grupo("0", datos);

			datos = Arrays.asList();
			datos = Arrays.asList(new Dato("PARA", "joseph.rodriguez@segurosbolivar.com"),
					new Dato("IDENTIFICACION", "1026262691"), new Dato("TIPODOC", "CC"));

			Grupo grupo1 = new Grupo("1", datos);

			request.setGrupo(Arrays.asList(grupo0, grupo1));

			// log.info(request.toString());
			ObjectMapper mapper = new ObjectMapper();
			try {
				log.info(mapper.writeValueAsString(request));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HttpEntity<RequestEventosDTO> httpEntity = new HttpEntity<>(request, headers);
			ResponseEventosDTO result = restTemplate.postForObject(urlString, httpEntity, ResponseEventosDTO.class);
			
			if (result != null) {
				log.info(result.toString());
			}
			
			
			log.info("Se ha enviado correo al cliente: " + idConsecutivo);
			
		} catch (Exception e) {
			log.error("Error en servicio de Eventos " + " | " + e.getMessage() + " | " + e.getClass() + " | "
					+ e.getStackTrace()[0]);
		}

	}
}
