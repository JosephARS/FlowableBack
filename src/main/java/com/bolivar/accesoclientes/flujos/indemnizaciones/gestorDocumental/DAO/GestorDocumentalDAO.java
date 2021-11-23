package com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.DAO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;

public interface GestorDocumentalDAO {

	ResponseWS cargarDocumentos(String idProceso, String idConsecutivo, List<MultipartFile> documentos);
	
	ResponseWS consultarDocumento(String bucket, String key );

	ResponseWS actualizarRutaDocumentos(String idConsecutivo, List<Object> rutaDocumentoList);
	
}
