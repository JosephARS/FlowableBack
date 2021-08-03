package com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.controller;

import java.util.List;
import java.util.Map;

import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;
import com.bolivar.accesoclientes.flujos.indemnizaciones.moduloGestion.service.ModuloGestionService;
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
											@PathVariable("primerItem") Integer primerItem){
    	ResponseWS oResponseWS = new ResponseWS();
    	
    	oResponseWS = moduloGestionService.obtenerListaProcesosHistorico(procesosFinalizados, cantidadItems, primerItem);
    			
    	return oResponseWS;
    }
    
    @GetMapping("/historico/procesos/pendientes/{cantidadItems}/{primerItem}")
    public ResponseWS obtenerListaProcesosPendientes(@PathVariable("cantidadItems") Integer cantidadItems,
											@PathVariable("primerItem") Integer primerItem){
    	
    	ResponseWS oResponseWS = new ResponseWS();
    	
    	oResponseWS = moduloGestionService.obtenerListaProcesosPendientes(cantidadItems, primerItem);
    			
    	return oResponseWS;
    }
    
    @GetMapping("/historico/procesos/anulados/{cantidadItems}/{primerItem}")
    public ResponseWS obtenerListaProcesosAnulados(@PathVariable("cantidadItems") Integer cantidadItems,
											@PathVariable("primerItem") Integer primerItem){
    	
    	ResponseWS oResponseWS = new ResponseWS();
    	
    	oResponseWS = moduloGestionService.obtenerListaProcesosAnulados(cantidadItems, primerItem);
    			
    	return oResponseWS;
    }
    
    @PostMapping("/usuario/tareaAtendida/{idTarea}/{idTareaDefinicion}/{idUsuario}")
    public ResponseWS completarTarea(@PathVariable("idTarea") String idTarea,
							    		@PathVariable("idTareaDefinicion") String idTareaDefinicion,
							    		@PathVariable("idUsuario") String idUsuario,
							    		@RequestBody VariablesProceso variablesProceso) {
										
    	ResponseWS oResponseWS = new ResponseWS();
    	
    	oResponseWS = moduloGestionService.completarTarea(idTarea, idTareaDefinicion, idUsuario, variablesProceso);
    	
    	return oResponseWS;
    	
    }
    
    @GetMapping("/procesos/historialCaso/{idProceso}")
    public ResponseWS obtenerHistorialCaso(@PathVariable("idProceso")String idProceso) {
    	
    	ResponseWS oResponseWS = new ResponseWS();
    	
    	oResponseWS = moduloGestionService.obtenerHistorialCaso(idProceso);
    			
    	return oResponseWS;
    }
    
    @GetMapping("/busqueda/{parametroBusqueda}/{valorBusqueda}/{itemPorPagina}/{primerItem}")
    public ResponseWS busquedaGeneral(@PathVariable("parametroBusqueda")String parametroBusqueda,
						    		@PathVariable("valorBusqueda")String valorBusqueda,
									@PathVariable("itemPorPagina") Integer itemPorPagina,
									@PathVariable("primerItem") Integer primerItem) {
    	
    	ResponseWS oResponseWS = new ResponseWS();
    	
    	oResponseWS = moduloGestionService.busquedaGeneral(parametroBusqueda, valorBusqueda, itemPorPagina, primerItem);
    			
    	return oResponseWS;
    }
    
    
}
