package com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.DAO;

import java.util.List;

import org.flowable.task.api.Task;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.model.Identificacion;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.VariablesProceso;



public interface ModuloGestionDAO {

	public ResponseWS obtenerTareasUsuario(String user, String nombreTarea,Integer cantidadItems, Integer primerItem);
	
	public ResponseWS cantidadTareasUsuario(String user);
	
	public ResponseWS obtenerProcesosIdentificacion(String identificacion,Integer cantidadItems, Integer primerItem);
	
	public ResponseWS obtenerRutaProceso(String IdProceso);
	
	public ResponseWS obtenerInfoProcesoDetalle(String IdProceso);
	
	public ResponseWS obtenerListaProcesosHistorico(Boolean procesosFinalizados, Integer cantidadItems, Integer primerItem);//Procesos activos o finalizados dependiendo de la variable 'procesosFinalizados'
	
	public ResponseWS obtenerListaProcesosAnulados(Integer cantidadItems, Integer primerItem);//Procesos anulados
	
	public ResponseWS obtenerListaProcesosPendientes(Integer cantidadItems, Integer primerItem);//Procesos que no est�n asignados porque est�n a la espera de alg�n evento
	
	//Completar tareas
	public ResponseWS completarTarea(String idTarea, String idTareaDefinicion, VariablesProceso variablesProceso);
	
}
