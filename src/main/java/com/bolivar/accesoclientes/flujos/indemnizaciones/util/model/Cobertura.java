package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import lombok.Data;

@Data
public class Cobertura {

	ObjCodigoValor coberturaSini;
	ObjCodigoValor consecuenciaCob;
	ObjCodigoValor scoreRiesgoCob;
	ObjCodigoValor clasMotorCasoCob;
	ObjCodigoValor motorDefiniCob;
	Long valorPretensionCob;	
	Long valorEvidenciaCob;
	Long valIndemnizarCob;
	
}
