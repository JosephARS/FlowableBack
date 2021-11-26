package com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.model;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResponseUploadBucketAWS {

	List<RutaDocumento> files;
	
}
