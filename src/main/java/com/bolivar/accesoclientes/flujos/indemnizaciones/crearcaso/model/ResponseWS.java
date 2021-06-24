package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ResponseWS {

	private TipoRespuesta tipoRespuesta;	//Enumerador: Puede ser Exito o error.
	private String mensaje = "";			//Mensaje: Contiene el mensaje en caso de devolver un error.
	private Consecutivos resultado;				//Contiene los datos resultado del procedimiento, en caso de que sea un solo objeto.
	
}
