package com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.DAO;

import java.util.Date;
import java.util.Map;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Analisis;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.VariablesProceso;



public interface ModuloGestionDAO {

	public ResponseWS obtenerTareasUsuario(String user, String nombreTarea,Integer cantidadItems, Integer primerItem);
	
	public ResponseWS cantidadTareasUsuario(String user);
	
	public ResponseWS obtenerProcesosIdentificacion(String identificacion,Integer cantidadItems, Integer primerItem);
	
	public ResponseWS obtenerRutaProceso(String idProceso);
	
	public ResponseWS obtenerInfoProcesoDetalle(String idProceso);
	
	public ResponseWS obtenerListaProcesosHistorico(Boolean procesosFinalizados, Integer cantidadItems, Integer primerItem, Date fechaInicio, Date fechaFin);//Procesos activos o finalizados dependiendo de la variable 'procesosFinalizados'
	
	public ResponseWS obtenerListaProcesosAnulados(Integer cantidadItems, Integer primerItem, Date fechaInicio, Date fechaFin);//Procesos anulados
	
	public ResponseWS obtenerListaProcesosPendientes(Integer cantidadItems, Integer primerItem, Date fechaInicio, Date fechaFin);//Procesos que no estan asignados porque estan a la espera de algun evento
	
	public ResponseWS obtenerHistorialCaso(String idProceso);
	
	public ResponseWS busquedaGeneral(String parametroBusqueda, String valorBusqueda, Integer cantidadItems, Integer primerItem);
	
	//Completar tareas
	public ResponseWS completarTarea(String idTarea, String idTareaDefinicion, String idUsuario, VariablesProceso variablesProceso);
	
	//Gestion de usuario
	public ResponseWS listarUsuarios(String idTareaDefinicion);
	
	public ResponseWS cambiarEstadoUsuario(Integer idUsuario, Integer estado);
	
	public ResponseWS reasignarUsuario(String idProceso, String usuarioAsignado, String idTarea);
	
	public ResponseWS ingresarNuevoAnalisis(String idProceso, Analisis analisis);
	
	public ResponseWS corregirActividadAnterior(VariablesProceso variablesProceso);
	
	public Map<String, String> calcularTiempoSolucion(Date fechaInicio, Date fechaFin);
	
	public ResponseWS procesosSuspendidos(Integer cantidadItems, Integer primerItem, Date fechaInicio, Date fechaFin);
	
	public ResponseWS procesosErrorWS(Integer cantidadItems, Integer primerItem, Date fechaInicio, Date fechaFin);
	
	public ResponseWS reintentarProcesoSuspendido(String idJob);
	
}
