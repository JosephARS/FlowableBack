package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@ToString
@Table(name="info_general_proceso")
public class InfoGeneralProceso implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	Long idINFO_GENERAL_PROCESO;
	@NaturalId
	String idProceso;
	String idConsecutivo;
	String identificacionAsegurado;
    @Type(type = "json")
    @Column(columnDefinition = "json")
    VariablesProceso  documento;					//Objeto del tipo ProcesoIndemnizacion
    @Type(type = "json")
    @Column(columnDefinition = "json")
    List<Analisis> historialAnalisis;
	
	public InfoGeneralProceso() {
		//super();
	}
	
}
