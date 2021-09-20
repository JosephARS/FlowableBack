package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;

import java.util.Date;
import java.util.List;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Documento;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.ObjCodigoValor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class NuevoCaso {
	

	String idProceso;
	String idConsecutivo;
	EstadoCaso estadoCaso;			//Codigo-Valor
	Date fechaSiniestro;
	ObjCodigoValor canalCreacion;	//Codigo-Valor
	String tipoDocumento;	
	String numeroDocumento;	
	String nombres;			
	String apellidos;		
	ObjCodigoValor ramoProducto;		//Codigo-Valor
	ObjCodigoValor producto;				//Codigo-Valor
	Long numeroPoliza;	
	ObjCodigoValor clasificacionCaso;	//Codigo-Valor
	List<ObjCodigoValor> causa;						//ArrayList[Codigo-Valor]
	List<ObjCodigoValor> consecuencia;		//ArrayList[Codigo-Valor]
	List<ObjCodigoValor> cobertura;				//ArrayList[Codigo-Valor]
	ObjCodigoValor riesgo;							//Codigo-Valor
	ObjCodigoValor resultadoScoreRiesgo;			//Codigo-Valor
	ObjCodigoValor resultadoMotorClasifica;	//Codigo-Valor
	ObjCodigoValor resultadoMotorDefi;				//Codigo-Valor
	List<Documento> documentos;
	String usuarioCreador;
	ObjCodigoValor clv;						//Codigo-Valor
	Long numeroContacto;
	String email;
	Long numeroSiniestro;
	Long valorReserva;
	Long valorPretension;	
	
	public NuevoCaso() {
		//super();
	}
	
	
}
