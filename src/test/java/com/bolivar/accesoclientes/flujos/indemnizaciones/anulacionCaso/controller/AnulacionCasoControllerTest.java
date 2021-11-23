package com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.controller.AnulacionCasoController;
import com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.service.AnulacionCasoService;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Anulacion;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(AnulacionCasoController.class)
//@ContextConfiguration(classes=AnulacionCasoController.class)

//@SpringBootTest()
//@AutoConfigureMockMvc
public class AnulacionCasoControllerTest {
	
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
	AnulacionCasoService anulacionCasoService;
    
    ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void AnularCaso() throws Exception{

		String idProceso = "29f7a8e2-0076-11ec-84e8-b07d648ec81d";
		
		Anulacion datosAnulacion = new Anulacion();
    	datosAnulacion.setUsuario("UsuarioPrueba");
    	datosAnulacion.setMotivoAnulacion("Pruebas");
    	datosAnulacion.setObservacion("ObservacionPrueba");
    	
    	ResponseWS oResponseWs = new ResponseWS();
    	oResponseWs.setTipoRespuesta(TipoRespuesta.Exito);
    	
    	
    	when(anulacionCasoService.anularCaso(idProceso, datosAnulacion)).thenReturn(oResponseWs);

		mockMvc.perform(put("/api/v1/procesos/anular/{IdProceso}", idProceso)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(datosAnulacion)))
	    	      .andExpect(status().isOk())
	    	      .andExpect(jsonPath("$.tipoRespuesta", org.hamcrest.Matchers.is("Exito")))
	    	      .andReturn();
	
		
	}
	
}
