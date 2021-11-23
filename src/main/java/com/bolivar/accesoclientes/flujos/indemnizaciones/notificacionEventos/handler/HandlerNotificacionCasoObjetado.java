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

import com.bolivar.accesoclientes.flujos.indemnizaciones.generarCartaObjecion.service.GenerarCartaObjecionService;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.model.ArchivoDTO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.service.GestorDocumentalService;
import com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model.Dato;
import com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model.DatoBinario;
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
public class HandlerNotificacionCasoObjetado implements JavaDelegate {

	@Autowired
	private Environment env;
	@Autowired
	GenerarCartaObjecionService generarCartaObjecionService;
	@Autowired
	GestorDocumentalService gestorDocumentalService;
	@Autowired
	InfoGeneralProcesoRepository infoProcesoRepository;
	
	@Override
	public void execute(DelegateExecution execution) {

	
		try {
			String idProceso = execution.getProcessInstanceId();
			
			Optional<InfoGeneralProceso> oInfoGeneralProceso = infoProcesoRepository.findByIdProceso(idProceso);
			
			String nombre = oInfoGeneralProceso.get().getDocumento().getAsegurado().getNombres();
			String apellido = oInfoGeneralProceso.get().getDocumento().getAsegurado().getApellidos();
			String producto = oInfoGeneralProceso.get().getDocumento().getInfoProducto().getRamo().getValor();
			
			String pdfBase64 = generarCartaObjecionService.generarCarta(nombre+apellido, producto);
			
			RestTemplate restTemplate = new RestTemplate();

			
			String NOTIFICACION_EVENTOS = "spring.profiles.notificacionEventos";
			String APLICACION = "servicios.notificacionEventos.objecion.aplicacion";
			String EVENTO = "servicios.notificacionEventos.objecion.evento";
			String API_KEY = "servicios.notificacionEventos.apiKey";

			String idConsecutivo = (String) execution.getVariable("idConsecutivo");


			String urlString = env.getProperty(NOTIFICACION_EVENTOS);
			
			System.out.println("URL:" + urlString);

			HttpHeaders headers = new HttpHeaders();
			headers.add("x-api-key", env.getProperty(API_KEY));

			RequestEventosDTO request = new RequestEventosDTO();
			request.setAplicacion(env.getProperty(APLICACION));
			request.setEvento(env.getProperty(EVENTO));

			List<Object> datos = Arrays.asList(new Dato("P_NOMBRE_CLIENTE", nombre),
												new Dato("P_ASUNTO", "Novedad en su proceso de reclamación"));
							

			Grupo grupo0 = new Grupo("0", datos);

			datos = Arrays.asList();
			datos = Arrays.asList(new Dato("PARA", "joseph.rodriguez@segurosbolivar.com"),
									new Dato("IDENTIFICACION",  oInfoGeneralProceso.get().getDocumento().getAsegurado().getNumeroDocumento()),
									new Dato("TIPODOC", oInfoGeneralProceso.get().getDocumento().getAsegurado().getTipoDocumento()));

			Grupo grupo1 = new Grupo("1", datos);

			datos = Arrays.asList();
			datos = Arrays.asList(new DatoBinario("BINARIO", "Resultado_" + idConsecutivo + ".PDF", pdfBase64));

			Grupo grupo2 = new Grupo("2", datos);

			
			request.setGrupo(Arrays.asList(grupo0, grupo1, grupo2));

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
				ArchivoDTO oArchivo = ArchivoDTO.builder().idConsecutivo(idConsecutivo).nombre("Carta_objecion").archivoBase64(pdfBase64).build();
				gestorDocumentalService.cargarArchivoBinario(idProceso, oArchivo);
				
			}
			log.info("Se ha enviado correo al cliente: " + idConsecutivo);			
			

		} catch (Exception e) {
			log.error("No se ha podido generar la carta de objecion" + e.getMessage() + "|" +e.getCause() + "|" + e.getStackTrace()[0]);
		}

	}
}
