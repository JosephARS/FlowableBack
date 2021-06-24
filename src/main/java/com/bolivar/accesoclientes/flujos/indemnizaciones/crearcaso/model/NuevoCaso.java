package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class NuevoCaso {
	
	EstadoCaso estadoCaso;			//Codigo-Valor
	Date fechaSiniestro;
	CanalCreacion canalCreacion;	//Codigo-Valor
	String tipoDocumento;	
	String numeroDocumento;	
	String nombres;			
	String apellidos;		
	RamoProducto ramoProducto;		//Codigo-Valor
	Producto producto;				//Codigo-Valor
	BigDecimal numeroPoliza;	
	ClasificacionCaso clasificacionCaso;	//Codigo-Valor
	List<Causa> causa;						//ArrayList[Codigo-Valor]
	List<Consecuencia> consecuencia;		//ArrayList[Codigo-Valor]
	List<Cobertura> cobertura;				//ArrayList[Codigo-Valor]
	Riesgo riesgo;							//Codigo-Valor
	ResultadoScoreRiesgo resultadoScoreRiesgo;			//Codigo-Valor
	ResultadoMotorClasifica resultadoMotorClasifica;	//Codigo-Valor
	ResultadoMotorDefi resultadoMotorDefi;				//Codigo-Valor
	List<Documento> documentos;
	String usuarioCreador;
	
	public NuevoCaso() {
		//super();
	}
	
	
}
