package com.bolivar.accesoclientes.flujos.indemnizaciones.util.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
@Table(name="tareas")
public class Tarea {

	@Id
	Integer idTarea;
	String idTareaDefinicion;
	String nombreTarea;
	String modulo;
	String tiempoAtencion;
	
}
