package com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.handler;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.model.ReqConseSini;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.model.ReqProcesoSini;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.model.RequestCrearSiniestro;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.model.ResponseCrearSiniestro;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.InfoGeneralProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Component
public class GestionarCrearSiniestroSimon implements JavaDelegate{
	 
	@Autowired
	private Environment env;
	
	@Autowired
	InfoGeneralProcesoRepository infoProcesoRepository;
	
	@Override
	    public void execute(DelegateExecution execution) throws HttpStatusCodeException {
		
		RequestCrearSiniestro request = new RequestCrearSiniestro();
		
		try {

			RestTemplate restTemplate = new RestTemplate();
			ObjectMapper mapper = new ObjectMapper();
			String idProceso = execution.getRootProcessInstanceId();
						
			Optional<InfoGeneralProceso> obtenerProceso = infoProcesoRepository.findByIdProceso(idProceso);
			
			String CREAR_SINIESTRO_SIMON = "spring.profiles.crearSiniestroSimon";
			//String urlString = "https://fz73xehwah.execute-api.us-east-1.amazonaws.com/dev/poliza/api/v1/siniestros";
			String urlString = env.getProperty(CREAR_SINIESTRO_SIMON);
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("x-api-key", "4Qsus5DlSD8gXesXfEBzN4ZBRrXaw67e4IQ6CFZX");
			
			SimpleDateFormat fecha = new SimpleDateFormat("mm-dd-yyyy");
			
			if(obtenerProceso.isPresent()) {				
				
				List<ReqConseSini> listaConsecuencia = Arrays.asList(new ReqConseSini(1,216,1,10000,1,1,1,0),
																	(new ReqConseSini(59,216,1,10000,1,1,1,0)));
				
				long compania = Long.parseLong(obtenerProceso.get().getDocumento().getInfoProducto().getCompania().getCodigo());
				long seccion = Long.parseLong(obtenerProceso.get().getDocumento().getInfoProducto().getProducto().getCodigo());
				long producto = Long.parseLong(obtenerProceso.get().getDocumento().getInfoProducto().getRamo().getCodigo());
				String poliza = obtenerProceso.get().getDocumento().getInfoProducto().getNumeroPoliza().toString();
				String tipoDoc = obtenerProceso.get().getDocumento().getAsegurado().getTipoDocumento();
				String nombre = obtenerProceso.get().getDocumento().getAsegurado().getNombres();
				String apellido = obtenerProceso.get().getDocumento().getAsegurado().getApellidos();
				Date fechaSini = obtenerProceso.get().getDocumento().getSiniestro().getFechaSiniestro();
				long causaCod = Long.parseLong(obtenerProceso.get().getDocumento().getInfoProducto().getCausa().get(0).getCodigo());
				long clv = Long.parseLong(obtenerProceso.get().getDocumento().getAsegurado().getClv().getCodigo());
				long clasificacionCaso = Long.parseLong(obtenerProceso.get().getDocumento().getInfoProceso().getClasificacionCaso().getCodigo());
				long motorDefi = Long.parseLong(obtenerProceso.get().getDocumento().getInfoProceso().getResultadoMotorDefi().getCodigo());
				
				ReqProcesoSini proceso = new ReqProcesoSini();
				proceso.setModulo("0");
				proceso.setProceso(71);
				
				request.setCompania(compania);
				request.setSeccion(seccion);
				request.setProducto(producto);
				request.setUsuario("52007029");
				request.setNumPol(poliza);
				request.setNumSecuPol("29733811023");
				request.setNumEnd(0);
				request.setCodRies(1);
				request.setTdocTercero(tipoDoc);
				request.setCodAseg("51938035");
				request.setNomAseg(nombre);
				request.setApeAseg(apellido);
				request.setFechaSini(fechaSini);
				request.setHoraSini("00:00");
				request.setDescSini("TEST");
				request.setCodCausaSini(causaCod);
				request.setClv(clv);
				request.setModeloRiesgo(1);
				request.setValorEncuesta(1);
				request.setMotorCaso(clasificacionCaso);
				request.setMotorDefinicion(motorDefi);
				request.setMotivoCambio("TEST_API");
				request.setConsecuencia(listaConsecuencia);
				request.setValidacion("S");
				request.setProceso(proceso);
	
				log.info("Creacion siniestro simon: "+ mapper.writeValueAsString(request));
	
				try {
					HttpEntity<RequestCrearSiniestro> httpEntity = new HttpEntity<>(request, headers);
					ResponseCrearSiniestro result = restTemplate.postForObject(urlString, httpEntity, ResponseCrearSiniestro.class);
					
					if (result != null) {

							System.out.println(result.getNumSini());
							int res = infoProcesoRepository.updateNumeroSiniestro(idProceso, result.getNumSini());
							System.out.println(res); 
									
					}
					log.info("Se ha creado siniestro en simón: " +  result);
				} catch (HttpClientErrorException e) {
					log.error("Error en la respuesta del servicio Crear Siniestro: " + e.getResponseBodyAsString() + "Codigo http error: " + e.getStatusCode().value());				
				}
			}

			
		} catch (Exception e) {
			log.error("Error en creacion siniestro Simon: " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]+ " | " + e.getLocalizedMessage() );


		}
	
	}

}
