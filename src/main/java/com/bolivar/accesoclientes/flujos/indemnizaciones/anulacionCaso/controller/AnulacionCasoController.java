package com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.service.AnulacionCasoService;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Anulacion;

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
public class AnulacionCasoController {
	
	AnulacionCasoService anulacionCasoService;
	
    @PutMapping("/procesos/anular/{IdProceso}")
    public ResponseWS anularCaso(@PathVariable("IdProceso") String idProceso,
    							@RequestBody Anulacion datosAnulacion){    	
    	
    	ResponseWS oResponseWS = anulacionCasoService.anularCaso(idProceso, datosAnulacion);
		return oResponseWS;
    	
    	
    }

}
