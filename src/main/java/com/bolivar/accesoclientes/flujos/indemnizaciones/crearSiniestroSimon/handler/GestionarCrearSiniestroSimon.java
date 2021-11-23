package com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.flowable.common.engine.api.FlowableException;
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

import com.bolivar.accesoclientes.flujos.indemnizaciones.actualizarValorReserva.service.ActualizarValorReservaService;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.model.ReqConseSini;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.model.ReqProcesoSini;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.model.RequestCrearSiniestro;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.model.ResponseCrearSiniestro;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.EstadoSolicitud;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.InfoGeneralProceso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
	
	@Autowired
	ActualizarValorReservaService actualizarValorReservaService;
	
	@Override
	    public void execute(DelegateExecution execution) throws HttpStatusCodeException {
		
		RequestCrearSiniestro request = new RequestCrearSiniestro();
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		long numeroSini = 0;
		String idProceso = execution.getRootProcessInstanceId();
		
		try {
			
			Optional<InfoGeneralProceso> variablesProceso = infoProcesoRepository.findByIdProceso(idProceso);
			
			String CREAR_SINIESTRO_SIMON = "spring.profiles.crearSiniestroSimon";
			String API_KEY = "servicios.crearSiniestroSimon.apiKey";
			String urlString = env.getProperty(CREAR_SINIESTRO_SIMON);
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("x-api-key", env.getProperty(API_KEY));

			
			Optional<Long> numeroSiniestro = Optional.ofNullable(variablesProceso.get().getDocumento().getSiniestro().getNumeroSiniestro());
			if(numeroSiniestro.isPresent()) {
				numeroSini = numeroSiniestro.get();
			}
			
			log.info("numero siniestro: "+ numeroSiniestro.get());
			
			if (numeroSini == 0 ) {
				log.info("compa: "+ variablesProceso.isPresent());
				log.info("numero siniestro: "+ "entro");
				List<ReqConseSini> listaConsecuencia = Arrays.asList(new ReqConseSini(1,216,1,10000,1,1,1,0),
						(new ReqConseSini(59,216,1,10000,1,1,1,0)));
				
				long compania = Long.parseLong(variablesProceso.get().getDocumento().getInfoProducto().getCompania().getCodigo());
				long seccion = Long.parseLong(variablesProceso.get().getDocumento().getInfoProducto().getProducto().getCodigo());
				long producto = Long.parseLong(variablesProceso.get().getDocumento().getInfoProducto().getRamo().getCodigo());
				String poliza = variablesProceso.get().getDocumento().getInfoProducto().getNumeroPoliza().toString();
				String tipoDoc = variablesProceso.get().getDocumento().getAsegurado().getTipoDocumento();
				String nombre = variablesProceso.get().getDocumento().getAsegurado().getNombres();
				String apellido = variablesProceso.get().getDocumento().getAsegurado().getApellidos();
				Date fechaSini = variablesProceso.get().getDocumento().getSiniestro().getFechaSiniestro();
				long causaCod = Long.parseLong(variablesProceso.get().getDocumento().getInfoProducto().getCausa().getCodigo());
				long clv = Long.parseLong(variablesProceso.get().getDocumento().getAsegurado().getClv().getCodigo());
				long clasificacionCaso = Long.parseLong(variablesProceso.get().getDocumento().getInfoProceso().getClasificacionCaso().getCodigo());
				long motorDefi = Long.parseLong(variablesProceso.get().getDocumento().getInfoProceso().getResultadoMotorDefi().getCodigo());

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
				try {
					log.info("Creacion siniestro simon: " + mapper.writeValueAsString(request));
				} catch (JsonProcessingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					HttpEntity<RequestCrearSiniestro> httpEntity = new HttpEntity<>(request, headers);
					ResponseCrearSiniestro result = restTemplate.postForObject(urlString, httpEntity,
							ResponseCrearSiniestro.class);

					if (result != null) {

						numeroSini = result.getNumSini();
						variablesProceso.get().getDocumento().getSiniestro().setNumeroSiniestro(numeroSini);
						System.out.println(numeroSiniestro);
						int res = infoProcesoRepository.updateNumeroSiniestro(idProceso, numeroSini);
						System.out.println(res);

					}
					log.info("Se ha creado siniestro en simón: " + result);
				} catch (HttpClientErrorException e) {
					log.error("ErrorWS :: en la respuesta del servicio Crear Siniestro: " + e.getResponseBodyAsString()	+ "Codigo http error: " + e.getStatusCode());
					
				}
				
			}
			
			
		long valorRva = actualizarValorReservaService.consultaServicioSimon(variablesProceso.get().getDocumento());	//Se actuliza el valor de reserva pendiente.
		
		if (valorRva > 0) {
			execution.setVariable("valorReserva", valorRva);
		}else {
			throw new Exception("Error consultando valor reserva");
		}

			
		} catch (Exception e) {
			log.error("Error en creacion siniestro Simon: " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]+ " | " );
			infoProcesoRepository.P_ACTUALIZAR_ESTADO(idProceso, EstadoSolicitud.ERROR_SW);
			throw new FlowableException(e.getMessage());	//Error lanzado para que el proceso quede suspendido en "Dead letter".

		}
	
	}

}
