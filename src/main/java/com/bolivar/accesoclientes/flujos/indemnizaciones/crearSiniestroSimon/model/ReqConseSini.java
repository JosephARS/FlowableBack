package com.bolivar.accesoclientes.flujos.indemnizaciones.crearSiniestroSimon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqConseSini {

	long codConsSini;
	long codCobSini;
	long valorEvidencia;
	long valorPretension;
	long modeloRiesgoPretension;
	long clasMotorCaso;
	long motorDefini;
	long valIndemnizar;
}
