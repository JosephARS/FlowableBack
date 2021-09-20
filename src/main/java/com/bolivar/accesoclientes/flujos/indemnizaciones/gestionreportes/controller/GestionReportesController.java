package com.bolivar.accesoclientes.flujos.indemnizaciones.gestionreportes.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestionreportes.service.GestionReportesService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/")
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GestionReportesController {

	GestionReportesService gestionReportesService;
	
    @GetMapping("/administrar/reporte/tareasActivas")
    public ResponseWS gestionReportesService() {
    	
    	return gestionReportesService.reporTareasActivas();
    }
    
}
