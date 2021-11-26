package com.bolivar.accesoclientes.flujos.indemnizaciones.anulacionCaso.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.InfoGeneralProcesoRepository;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository.UsuariosRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


//@WebMvcTest(AnulacionCasoController.class)
//@ContextConfiguration(classes=AnulacionCasoController.class)

@RunWith(SpringRunner.class)
@SpringBootTest()
//@AutoConfigureMockMvc
public class AnulacionCasoControllerTest {
	
//    @Autowired
//    private MockMvc mockMvc;
    
    @Mock
	InfoGeneralProcesoRepository infoProcesoRepository;
    @Mock
    UsuariosRepository usuariosRepository;
    
    @Mock
    AnulacionCasoService anulacionCasoService;
    

    @InjectMocks
	AnulacionCasoController anulacionCasoController;
    
//    @Before
//    public void prueba() {
//    	when(anulacionCasoService.anularCaso(any(), null)))
//    }
    
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

    	ResponseWS oResponseWs2 = anulacionCasoController.anularCaso(idProceso, datosAnulacion);
    	
    	System.out.println(oResponseWs2.getTipoRespuesta());
    	
    	assertEquals(oResponseWs2.getTipoRespuesta(), TipoRespuesta.Exito);
    	
    	
//		mockMvc.perform(put("/api/v1/procesos/anular/{IdProceso}", idProceso)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(mapper.writeValueAsString(datosAnulacion)))
//	    	      .andExpect(status().isOk())
//	    	      .andExpect(jsonPath("$.tipoRespuesta", org.hamcrest.Matchers.is("Exito")))
//	    	      .andReturn();
	
		
	}
	
}
