package com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.service.GestorDocumentalService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/")
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class GestorDocumentalController {

	GestorDocumentalService gestorDocumentalService;
	
	@Operation(summary = "Cargar nuevos archivos al bucket")
    @PostMapping(value = "/documentos/cargar" )
	public ResponseWS cargarDocumentos(@RequestParam(value = "idConsecutivo", required=true) String idConsecutivo,
										@RequestParam(value = "idProceso", required=true) String idProceso,
										@RequestPart("file") List<MultipartFile> documentos){
		
		log.info(idConsecutivo + " / " + documentos.size());
		log.info(idConsecutivo + " / " + documentos.get(0).getContentType()
		+ " / " + documentos.get(0).getContentType()
		+ " / " + documentos.get(0).getName()
		+ " / " + documentos.get(0).getOriginalFilename()
		+ " / " + documentos.get(0).getResource()
		);
		
		return gestorDocumentalService.cargarDocumentos(idProceso, idConsecutivo, documentos);
		
	}
	
	
	@Operation(summary = "Actualizar ruta de documentos del caso")
    @PutMapping("/documentos/actualizaRuta")
	public ResponseWS actualizarRutaDocumentos(@RequestParam(value = "idConsecutivo", required=true) String idConsecutivo,
												@RequestBody List<Object> rutaDocumentoList){	
		
		return gestorDocumentalService.actualizarRutaDocumentos(idConsecutivo, rutaDocumentoList);
		
	}
	
	
	@Operation(summary = "Consultar archivos de un bucket")
    @GetMapping(value = "/documentos/consulta")
	public ResponseWS consultarDocumento(@RequestParam(value = "Bucket", required=true) String bucket,
										@RequestParam(value = "Key", required=true) String key) {
		
		return gestorDocumentalService.consultarDocumento(bucket, key);
		
	}
	
}
