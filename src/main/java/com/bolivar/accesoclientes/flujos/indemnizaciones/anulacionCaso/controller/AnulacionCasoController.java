package com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.service.AnulacionCasoService;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Anulacion;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/")
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnulacionCasoController {
	
	@Autowired
	AnulacionCasoService anulacionCasoService;
	
	@Operation(summary = "Anular un proceso activo", description = "Anulacion pruebitas")
    @PutMapping("/procesos/anular/{IdProceso}")
    public ResponseWS anularCaso(@PathVariable("IdProceso") String idProceso,
    							@RequestBody Anulacion datosAnulacion){    	
    	
    	return anulacionCasoService.anularCaso(idProceso, datosAnulacion);
    	
    	
    }

}
