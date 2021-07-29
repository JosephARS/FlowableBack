package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.CanalCreacion;

import liquibase.pro.packaged.L;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class NuevoCaso {
	

	String idProceso;
	String idConsecutivo;
	EstadoCaso estadoCaso;			//Codigo-Valor
	Date fechaSiniestro;
	CanalCreacion canalCreacion;	//Codigo-Valor
	String tipoDocumento;	
	String numeroDocumento;	
	String nombres;			
	String apellidos;		
	RamoProducto ramoProducto;		//Codigo-Valor
	Producto producto;				//Codigo-Valor
	Long numeroPoliza;	
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
	CLV clv;						//Codigo-Valor
	Long numeroContacto;
	String email;
	Long numeroSiniestro;
	Long valorReserva;
	Long valorPretension;	
	
	public NuevoCaso() {
		//super();
	}
	
	
}
