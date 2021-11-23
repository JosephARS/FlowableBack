package com.bolivar.accesoclientes.flujos.indemnizaciones.respuestaPreliquidacionCliente.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.respuestaPreliquidacionCliente.service.RespuestaPreliquidacionClienteService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1")
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RespuestaPreliquidacionClienteController {

	RespuestaPreliquidacionClienteService respuestaPreliquidacionClienteService;
	
	@Operation(summary = "Notificar al flujo si el cliente acepta preliquidación")
    @PutMapping("/preliquidacion/{idConsecutivo}/{clienteAcepta}")
    public ResponseWS mensajePreliquidacion(@PathVariable("clienteAcepta") Boolean clienteAcepta, @PathVariable("idConsecutivo") String idConsecutivo){

		return respuestaPreliquidacionClienteService.mensajePreliquidacion(idConsecutivo, clienteAcepta);
	}
}
