package com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.flowable.task.api.Task;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;
import com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.service.ModuloGestionService;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Analisis;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.VariablesProceso;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1")
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ModuloGestionController {

	
	ModuloGestionService moduloGestionService;
	
    @GetMapping("/tareas/{nombreUsuario}/{nombreTarea}/{cantidadItems}/{primerItem}")
    public ResponseWS obtenerTareasUsuario(@PathVariable("nombreUsuario") String nombreUsuario,
								    		@PathVariable("nombreTarea") String nombreTarea,
											@PathVariable("cantidadItems") Integer cantidadItems,
											@PathVariable("primerItem") Integer primerItem){
								    	
    	ResponseWS oResponseWS = new ResponseWS();
    	
    	oResponseWS = moduloGestionService.obtenerTareasUsuario(nombreUsuario, nombreTarea, cantidadItems, primerItem);
    	System.out.println(oResponseWS);
    	
		return oResponseWS;
    	
    	
    }
    
    @GetMapping("/tareas/abiertas/{nombreUsuario}")
    public ResponseWS cantidadTareasUsuario(@PathVariable("nombreUsuario") String nombreUsuario){
    	
    	ResponseWS oResponseWS = new ResponseWS();
    	
    	oResponseWS = moduloGestionService.cantidadTareasUsuario(nombreUsuario);
    	System.out.println(oResponseWS);
    	
		return oResponseWS;
    	
    	
    }
    
    @GetMapping("/procesos/abiertos/{identificacionAsegurado}/{cantidadItems}/{primerItem}")
    public ResponseWS obtenerProcesosIdentificacion(@PathVariable("identificacionAsegurado") String identificacionAsegurado,
											@PathVariable("cantidadItems") Integer cantidadItems,
											@PathVariable("primerItem") Integer primerItem){
    	
    	ResponseWS oResponseWS = new ResponseWS();
    	
    	oResponseWS = moduloGestionService.obtenerProcesosIdentificacion(identificacionAsegurado, cantidadItems, primerItem);
    	System.out.println(oResponseWS);
    	
		return oResponseWS;
    	
    	
    }
    
    @GetMapping("/procesos/infoDetalle/{idProceso}")    
    public ResponseWS obtenerInfoProcesoDetalle(@PathVariable("idProceso")String idProceso) {
    
    	ResponseWS oResponseWS = new ResponseWS();
    	
    	oResponseWS = moduloGestionService.obtenerInfoProcesoDetalle(idProceso);
    			
    	return oResponseWS;
    	
    }
    
    @GetMapping("/procesos/infoDetalle/ruta/{idProceso}")
    public ResponseWS obtenerRutaProceso(@PathVariable("idProceso")String idProceso) {
    	
    	ResponseWS oResponseWS = new ResponseWS();
    	
    	oResponseWS = moduloGestionService.obtenerRutaProceso(idProceso);
    			
    	return oResponseWS;
    }
    
    
    @GetMapping("/historico/procesos/{procesosFinalizados}/{cantidadItems}/{primerItem}")
    public ResponseWS obtenerHistoriaProceso(@PathVariable("procesosFinalizados")Boolean procesosFinalizados,
											@PathVariable("cantidadItems") Integer cantidadItems,
											@PathVariable("primerItem") Integer primerItem,
											@RequestParam(value = "fechaInicio", required=false) Date fechaInicio,
											@RequestParam(value = "fechaFin", required=false) Date fechaFin){
    	
    	
    	return moduloGestionService.obtenerListaProcesosHistorico(procesosFinalizados, cantidadItems, primerItem, fechaInicio,fechaFin);
    			
    }
    
    @GetMapping("/historico/procesos/pendientes/{cantidadItems}/{primerItem}")
    public ResponseWS obtenerListaProcesosPendientes(@PathVariable("cantidadItems") Integer cantidadItems,
											@PathVariable("primerItem") Integer primerItem,
											@RequestParam(value = "fechaInicio", required=false) Date fechaInicio,
											@RequestParam(value = "fechaFin", required=false) Date fechaFin){
    	System.out.println(fechaInicio + "/"+ fechaFin);
    	return moduloGestionService.obtenerListaProcesosPendientes(cantidadItems, primerItem, fechaInicio,fechaFin);

    }
    
    @GetMapping("/historico/procesos/anulados/{cantidadItems}/{primerItem}")
    public ResponseWS obtenerListaProcesosAnulados(@PathVariable("cantidadItems") Integer cantidadItems,
											@PathVariable("primerItem") Integer primerItem,
											@RequestParam(value = "fechaInicio", required=false) Date fechaInicio,
											@RequestParam(value = "fechaFin", required=false) Date fechaFin){
    	
    	return  moduloGestionService.obtenerListaProcesosAnulados(cantidadItems, primerItem, fechaInicio,fechaFin);
    			

    }
    
    @GetMapping("/historico/procesos/suspendidos/{cantidadItems}/{primerItem}")
    public ResponseWS obtenerListaProcesosSuspendidos(@PathVariable("cantidadItems") Integer cantidadItems,
											@PathVariable("primerItem") Integer primerItem,
											@RequestParam(value = "fechaInicio", required=false) Date fechaInicio,
											@RequestParam(value = "fechaFin", required=false) Date fechaFin){
    	
    	return  moduloGestionService.procesosSuspendidos(cantidadItems, primerItem, fechaInicio, fechaFin);
    			
    }
    
    @GetMapping("/historico/procesos/errorws/{cantidadItems}/{primerItem}")
    public ResponseWS obtenerListaProcesosErrorWS(@PathVariable("cantidadItems") Integer cantidadItems,
											@PathVariable("primerItem") Integer primerItem,
											@RequestParam(value = "fechaInicio", required=false) Date fechaInicio,
											@RequestParam(value = "fechaFin", required=false) Date fechaFin){
    	
    	return  moduloGestionService.procesosErrorWS(cantidadItems, primerItem, fechaInicio, fechaFin);		

    }
    
    @PostMapping("/usuario/tareaAtendida/{idTarea}/{idTareaDefinicion}/{idUsuario}")
    public ResponseWS completarTarea(@PathVariable("idTarea") String idTarea,
							    		@PathVariable("idTareaDefinicion") String idTareaDefinicion,
							    		@PathVariable("idUsuario") String idUsuario,
							    		@RequestBody VariablesProceso variablesProceso) {
    	
    	return moduloGestionService.completarTarea(idTarea, idTareaDefinicion, idUsuario, variablesProceso);
    	
    }
    
    @GetMapping("/procesos/historialCaso/{idProceso}")
    public ResponseWS obtenerHistorialCaso(@PathVariable("idProceso")String idProceso) {
    	
    	return moduloGestionService.obtenerHistorialCaso(idProceso);
    			
    }
    
    @GetMapping("/busqueda/{parametroBusqueda}/{valorBusqueda}/{itemPorPagina}/{primerItem}")
    public ResponseWS busquedaGeneral(@PathVariable("parametroBusqueda")String parametroBusqueda,
						    		@PathVariable("valorBusqueda")String valorBusqueda,
									@PathVariable("itemPorPagina") Integer itemPorPagina,
									@PathVariable("primerItem") Integer primerItem) {
    	
    	return moduloGestionService.busquedaGeneral(parametroBusqueda, valorBusqueda, itemPorPagina, primerItem);
    
    }
    
    
    @GetMapping("/procesos/usuarios/{idTareaDefinicion}")
    public ResponseWS listarUsuarios(@PathVariable("idTareaDefinicion")String idTareaDefinicion) {
        	
    	return moduloGestionService.listarUsuarios(idTareaDefinicion);
    }
    
    
    @PutMapping("/procesos/reasignar/{idProceso}/{idUsuario}/{idTarea}")
    public ResponseWS reasignarUsuario(@PathVariable("idProceso")String idProceso,
    									@PathVariable("idUsuario") String idUsuario,
    									@PathVariable("idTarea") String idTarea) {
        	
    	return moduloGestionService.reasignarUsuario(idProceso, idUsuario, idTarea);
    }
    
    @PutMapping("/usuarios/gestion/{idUsuario}/{estado}")
    public ResponseWS cambiarEstadoUsuario(@PathVariable("idUsuario") Integer idUsuario, @PathVariable("estado") Integer estado ) {
    	return moduloGestionService.cambiarEstadoUsuario(idUsuario, estado);
    }
    
    @PostMapping("/corregirActividad")
	public ResponseWS corregirActividadAnterior(@RequestBody VariablesProceso variablesProceso) {
		return moduloGestionService.corregirActividadAnterior(variablesProceso);
	}
    
    @PutMapping("/procesos/analisis/{idProceso}")
    public ResponseWS ingresarNuevoAnalisis(@PathVariable("idProceso")String idProceso,
    										@RequestBody Analisis analisis) {
        	
    	return moduloGestionService.ingresarNuevoAnalisis(idProceso, analisis);
    }
    
    @PostMapping("/historico/procesos/suspendidos/{idJob}")
    public ResponseWS obtenerListaProcesosSuspendidos(@PathVariable("idJob") String idJob){
    	
    	return  moduloGestionService.reintentarProcesoSuspendido(idJob);
    			
    }
}
