package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@ToString
public class NuevoCasobd {
	
	@Id
	Long idINFO_GENERAL_PROCESO;
	@NaturalId
	String idProceso;
	String idConsecutivo;
	String identificacionAsegurado;
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    String  documento;					//Este es un objeto del tipo ProcesoIndemnizacion
	
	public NuevoCasobd() {
		//super();
	}
	
	
}
