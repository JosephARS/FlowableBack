package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.NuevoCaso;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.service.CrearCasoService;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.VariablesProceso;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/")
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CrearCasoController {

	CrearCasoService crearCasoService;
	
    @PostMapping(path = "/despliegue")
    public String deployWorkflow() {
    	crearCasoService.deployProcessDefinition();
    	System.out.println("ok");
    	return "ok";
    }
    
    @PostMapping(path = "/casos")
    public ResponseWS registrarNuevoCaso(@RequestBody VariablesProceso procesoIndemnizacion) {
    	ResponseWS oResponseWS = crearCasoService.registrarNuevoCaso(procesoIndemnizacion);
    	if (oResponseWS.getResultado() != null) {
        	String consecutivo = oResponseWS.getResultado().toString();
        	log.info("Caso creado " + consecutivo);
		}

    	return oResponseWS;
    }
	
    @GetMapping("/procesos/{IdProceso}")		//VERIFICAR LAS TAREAS POR LAS QUE HA PASADO UNA SOLICITUD
    public List<Object> checkState(@PathVariable("IdProceso") String processId){
    	log.info("Historico proceso: " + processId);
    	return crearCasoService.trazaHistoricoProceso(processId);
    }
    

    
}
