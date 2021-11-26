package com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FilesDTO {

	String taxonomyId;
	String nombreArchivo;
	String contentType;
	MetadataDTO metadata;
	
	
}
