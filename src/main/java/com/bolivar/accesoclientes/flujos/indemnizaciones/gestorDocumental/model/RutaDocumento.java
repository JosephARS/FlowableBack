package com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RutaDocumento {

	String uploadUrl;
	String bucket;
	String route;
	
}
