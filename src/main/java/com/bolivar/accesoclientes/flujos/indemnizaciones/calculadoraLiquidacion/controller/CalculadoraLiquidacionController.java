package com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolivar.accesoclientes.flujos.indemnizaciones.calculadoraLiquidacion.service.CalculadoraLiquidacionService;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;

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
public class CalculadoraLiquidacionController {

	CalculadoraLiquidacionService calculadoraLiquidacionService;
	
    @PutMapping("/preliquidacion/{IdProceso}/{clienteAcepta}")
    public ResponseWS mensajePreliquidacion(@PathVariable("clienteAcepta") Boolean clienteAcepta, @PathVariable("IdProceso") String idProceso){    	

	ResponseWS oResponseWS = calculadoraLiquidacionService.mensajePreliquidacion(idProceso, clienteAcepta);
	return oResponseWS;


}
    
}
