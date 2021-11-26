package com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.DAO.GestorDocumentalDAO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.model.ArchivoDTO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.model.FilesDTO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.model.MetadataDTO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.model.RequestUploadBucketAWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.model.ResponseDownloadBucketAWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.model.ResponseUploadBucketAWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GestorDocumentalService implements GestorDocumentalDAO {

	ProcessEngine processEngine;
	
	private Environment env;
	
	InfoGeneralProcesoRepository infoProcesoRepository;
	
	public ResponseWS cargarDocumentos(String idProceso, String idConsecutivo, List<MultipartFile> documentos) {
		
		log.info(idConsecutivo + " / " + documentos);
		
		ResponseWS oResponseWS = new ResponseWS();
		
		RestTemplate restTemplate = new RestTemplate();
		RequestUploadBucketAWS request = new RequestUploadBucketAWS();
		List<FilesDTO> filesList = new ArrayList<FilesDTO>();
		MetadataDTO metadata = new MetadataDTO();
		metadata.setClassification("soportes");
		metadata.setCode("001");
		metadata.setType("comun");
		metadata.setFuction("generales");
		metadata.setAplication("app-Flowable");

		documentos.forEach(doc ->{
			
			FilesDTO file = new FilesDTO();
			file.setTaxonomyId("IndemnizacionesGenerales");
			file.setNombreArchivo(idConsecutivo + "/" + doc.getOriginalFilename());
			file.setContentType("application/pdf");
			file.setMetadata(metadata);

			filesList.add(file);
			
		});
		
		request.setFiles(filesList);
		log.info(request.toString());
		
		String GESTOR_DOCUMENTAL_UPLOAD = "spring.profiles.gestorDocumentalUpload";
		
		String urlString = env.getProperty(GESTOR_DOCUMENTAL_UPLOAD);
		
		
		try {
			/**
			 * Se obtienen las URL donde se subirán los archivos a AWS.
			 */
			
			HttpEntity<RequestUploadBucketAWS> httpEntity = new HttpEntity<>(request);
			ResponseEntity<ResponseUploadBucketAWS> result = restTemplate.postForEntity(urlString, httpEntity,ResponseUploadBucketAWS.class);

			if (result.getStatusCode().is2xxSuccessful()) {

				log.info("Se ha creado URL para carga de documentos: " + result);
				
				/**
				 * Aqui se carga los documentos en las URL que se generaron previamente en AWS.
				 */
				List<Object> oRutasList = new ArrayList<Object>();
				
				List<String> archivosconError = new ArrayList<>();
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_PDF);
				
				for (int i = 0; i < result.getBody().getFiles().size(); i++) {
					
//					byte[] binario = Base64.getDecoder().decode(documentos.get(i).getDocumentoBin());
					byte[] binario = documentos.get(i).getBytes();
//					String binario = Base64.getEncoder().encodeToString(documentos.get(i).getBytes());
					
					URI	urlAWS = new URI(result.getBody().getFiles().get(i).getUploadUrl());

					HttpEntity<Object> httpEntity2 = new HttpEntity<>(binario, headers);
					ResponseEntity<Object> result2 = restTemplate.exchange(urlAWS, HttpMethod.PUT, httpEntity2, Object.class);
					if (result2.getStatusCode().is2xxSuccessful()) {
						log.info("Se cargó archivo : " + documentos.get(i).getOriginalFilename());
						Map<String, String> respuestaMap =  new HashMap<String, String>();
						respuestaMap.put("bucket", result.getBody().getFiles().get(i).getBucket());
						respuestaMap.put("route", result.getBody().getFiles().get(i).getRoute());
						log.info(respuestaMap.toString());
						oRutasList.add(respuestaMap);
						
					} else {
						log.info("No se ha podido cargar archivo : " + documentos.get(i).getOriginalFilename());
						archivosconError.add(documentos.get(i).getOriginalFilename());
					}
										
				}
				
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				System.out.println(infoProcesoRepository.P_INGRESAR_RUTA_DOCUMENTO(idProceso, mapper.writeValueAsString(oRutasList)));	//Se actualiza la ruta del documento en la BD de Flowable.
				
				if (archivosconError.isEmpty()) {
					
					oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
					oResponseWS.setMensaje("Archivos cargados correctamente");
					oResponseWS.setResultado(oRutasList);
				}else {
					oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
					oResponseWS.setMensaje("Algunos archivos no se cargaron correctamente: " + archivosconError);
					oResponseWS.setResultado(archivosconError);
				}
	
				
			}else {
				oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
				oResponseWS.setMensaje("No se han podido generar URL para cargar archivos");
			}
			
		} catch (HttpClientErrorException e) {
			log.error("ErrorWS :: en la respuesta del servicio Gestor documental: " + e.getResponseBodyAsString()	+ "Codigo http error: " + e.getStatusCode() + " | " + e.getStackTrace()[0]);
			
		} catch (URISyntaxException e) {
			log.error("Error en servicio Gestor documental: " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
		} catch (IOException e) {
			log.error("Error en servicio Gestor documental: " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
		}
		
		return oResponseWS;
		
		
	}

	@Override
	public ResponseWS actualizarRutaDocumentos(String idConsecutivo, List<Object> rutaDocumentoList) {
		
		ResponseWS oResponseWS = new ResponseWS();
		
		HistoryService historyService = processEngine.getHistoryService();

		HistoricProcessInstance proceso = historyService.createHistoricProcessInstanceQuery()	// Se recupera el ID del flujo de flowable para ese consecutivo.
				.variableValueEqualsIgnoreCase("idConsecutivo", idConsecutivo)
			    .singleResult();
		
		String idProceso = proceso.getId();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		try {
			System.out.println(infoProcesoRepository.P_INGRESAR_RUTA_DOCUMENTO(idProceso, mapper.writeValueAsString(rutaDocumentoList)));
			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
			oResponseWS.setMensaje("Rutas actualizadas correctamente");
		} catch (JsonProcessingException e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("No se han podido actualizar las rutas: " + e.getMessage());
			log.error("Error en servicio Gestor documental: " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
		}
		
		return oResponseWS;
	}
	
	

	@Override
	public ResponseWS consultarDocumento( String bucket, String key) {
		
		ResponseWS oResponseWS = new ResponseWS();
		
		RestTemplate restTemplate = new RestTemplate();
		
		String GESTOR_DOCUMENTAL_DOWNLOAD = "spring.profiles.gestorDocumentalDownload";
		
		String urlString = env.getProperty(GESTOR_DOCUMENTAL_DOWNLOAD);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlString)
		        .queryParam("Bucket",bucket)
		        .queryParam("Key", key);
		
		try {
//			ResponseEntity<ResponseDownloadBucketAWS> result = restTemplate.getForEntity(builder.toUriString(), ResponseDownloadBucketAWS.class );
			ResponseEntity<ResponseDownloadBucketAWS> result = restTemplate.getForEntity(URLDecoder.decode(builder.toUriString(), StandardCharsets.UTF_8), ResponseDownloadBucketAWS.class );
			String urlDescarga = result.getBody().getDownloadURL();
			
			log.info(urlDescarga);

			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
			oResponseWS.setResultado(urlDescarga);
			
		}catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("No ha podido acceder al documento");
			log.error("No ha podido acceder al documento: " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
		}
		
		
		return oResponseWS;
	}
	
	
	public String cargarArchivoBinario(String idProceso, ArchivoDTO documento) {

		String respuesta = "";
		RestTemplate restTemplate = new RestTemplate();
		RequestUploadBucketAWS request = new RequestUploadBucketAWS();
		List<FilesDTO> filesList = new ArrayList<FilesDTO>();
		
		
		MetadataDTO metadata = new MetadataDTO();
		metadata.setClassification("soportes");
		metadata.setCode("001");
		metadata.setType("comun");
		metadata.setFuction("generales");
		metadata.setAplication("app-Flowable");
			
		FilesDTO file = new FilesDTO();
		file.setTaxonomyId("IndemnizacionesGenerales");
		file.setNombreArchivo(documento.getIdConsecutivo() + "/" + documento.getNombre());
		file.setContentType("application/pdf");
		file.setMetadata(metadata);
		filesList.add(file);

		request.setFiles(filesList);
		
		String GESTOR_DOCUMENTAL_UPLOAD = "spring.profiles.gestorDocumentalUpload";
		String urlString = env.getProperty(GESTOR_DOCUMENTAL_UPLOAD);
		
		
		try {
			/**
			 * Se obtienen las URL donde se subirán los archivos a AWS.
			 */
			
			HttpEntity<RequestUploadBucketAWS> httpEntity = new HttpEntity<>(request);
			ResponseEntity<ResponseUploadBucketAWS> result = restTemplate.postForEntity(urlString, httpEntity,ResponseUploadBucketAWS.class);

			if (result.getStatusCode().is2xxSuccessful()) {

				log.info("Se ha creado URL para carga de documentos: " + result);
				
				/**
				 * Aqui se carga los documentos en las URL que se generaron previamente en AWS.
				 */
				List<Object> oRutasList = new ArrayList<Object>();
				
				List<String> archivosconError = new ArrayList<>();
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_PDF);
				
				for (int i = 0; i < result.getBody().getFiles().size(); i++) {
					
					byte[] binario = Base64.getDecoder().decode(documento.getArchivoBase64());
					
					URI	urlAWS = new URI(result.getBody().getFiles().get(i).getUploadUrl());

					HttpEntity<Object> httpEntity2 = new HttpEntity<>(binario, headers);
					ResponseEntity<Object> result2 = restTemplate.exchange(urlAWS, HttpMethod.PUT, httpEntity2, Object.class);
					if (result2.getStatusCode().is2xxSuccessful()) {
						log.info("Se cargó archivo : " + documento.getNombre());
						Map<String, String> respuestaMap =  new HashMap<String, String>();
						respuestaMap.put("bucket", result.getBody().getFiles().get(i).getBucket());
						respuestaMap.put("route", result.getBody().getFiles().get(i).getRoute());
						log.info(respuestaMap.toString());
						oRutasList.add(respuestaMap);
						
					} else {
						log.info("No se ha podido cargar archivo : " + documento.getNombre());
						archivosconError.add(documento.getNombre());
					}
										
				}
				
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				System.out.println(infoProcesoRepository.P_INGRESAR_RUTA_DOCUMENTO(idProceso, mapper.writeValueAsString(oRutasList)));	//Se actualiza la ruta del documento en la BD de Flowable.
				
				if (archivosconError.isEmpty()) {
					respuesta = TipoRespuesta.Exito.toString();
				}else {
					respuesta = TipoRespuesta.Error.toString();
				}
	
				
			}else {
				respuesta = "No se han podido generar URL para cargar archivos";
				log.info(respuesta);
			}
			
		} catch (HttpClientErrorException e) {
			log.error("ErrorWS :: en la respuesta del servicio Gestor documental: " + e.getResponseBodyAsString()	+ "Codigo http error: " + e.getStatusCode() + " | " + e.getStackTrace()[0]);
			
		} catch (URISyntaxException e) {
			log.error("Error en servicio Gestor documental: " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
		} catch (IOException e) {
			log.error("Error en servicio Gestor documental: " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
		}		
		
		return respuesta;
		
	}
	
}
