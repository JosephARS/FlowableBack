package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class NuevoCaso {
	
	String estadoCaso;
	String fechaCreacion;
	String canalCreacion;
	String tipoDocumento;
	String numeroDocumento;
	String nombres;
	String apellidos;
	BigDecimal ramoProducto;
	BigDecimal producto;
	BigDecimal numeroPoliza;
	String clasificacionCaso;
	String causa;
	String consecuencia;
	String cobertura;
	Map<String,String> documentos;
	
	public NuevoCaso() {
		//super();
	}
	
	
}
