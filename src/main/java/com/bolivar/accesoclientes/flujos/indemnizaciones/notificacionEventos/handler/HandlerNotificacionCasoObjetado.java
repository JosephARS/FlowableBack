package com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.handler;

import java.util.Arrays;
import java.util.List;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model.Dato;
import com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model.DatoBinario;
import com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model.Grupo;
import com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model.RequestEventosDTO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.notificacionEventos.model.ResponseEventosDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HandlerNotificacionCasoObjetado implements JavaDelegate {

	@Autowired
	private Environment env;

	@Override
	public void execute(DelegateExecution execution) {

		try {

			RestTemplate restTemplate = new RestTemplate();

			String NOTIFICACION_EVENTOS = "spring.profiles.notificacionEventos";

			String idConsecutivo = (String) execution.getVariable("idConsecutivo");

			String urlString = "https://fz73xehwah.execute-api.us-east-1.amazonaws.com/dev/notificacion/api/v1/mensajeria/eventos/mensajes";

			//String urlString = env.getProperty(NOTIFICACION_EVENTOS);
			
			System.out.println("URL:" + urlString);

			HttpHeaders headers = new HttpHeaders();
			headers.add("x-api-key", "1KN83VVMjx9l7fQIATjnR6RtvJbc4xxm284tuda8");

			RequestEventosDTO request = new RequestEventosDTO();
			request.setAplicacion("CGZ27OA4JIDD6A8");
			request.setEvento("QUT7C5I6LGDAQ7T");

			List<Object> datos = Arrays.asList(new Dato("P_NOMBRE", "Nombre"), new Dato("P_SOLICITUD", "12345"),
					new Dato("P_SOLICITANTE", "11120360010"), new Dato("P_INMUEBLE", "CASA FINCA"),
					new Dato("P_ARRIENDO", "1.500.000"), new Dato("P_ADMINISTRACION", "600.000"),
					new Dato("ENNOMBREDE",
							"El Libertador<operaciones.analisis@ellibertador.co>,comunicacioneselectronicas@ellibertador.co"),
					new Dato("P_ASUNTO", "12345"), new Dato("P_RESULTADO", "Aprobada"),
					new DatoBinario("BINARIO", "Resultado.PDF",
							"\nJVBERi0xLjQKJeLjz9MKMiAwIG9iago8PC9MZW5ndGggNTExL0ZpbHRlci9GbGF0ZURlY29kZT4+c3RyZWFtCnicdVS5btwwEO31FVO4SBbGLi+d3azENWhIokJSgl3aMFIECHL8YcrU+aGMDisr2ZEAAeS89+ZxOKMf0TlEMgGZKQgvkQ7Rp+h04cAZhM9jjAOjl0NGmJw2v0YfSqcrc8a2tKA9+B5o/TF8+QfNQabpBBX8xMRJMK6A5QVLC5ZfI2OQCZ81bVUA4yzLcybTawwX2Yry+q531kNpG+1KgzXlP9vaDOh2siqbCEG7hnQZEyJJxA4j4wkzGI+bhEqusfI6IMY6vQZ6G9AXwHeaQk7hAz2JSuMtna1xpy8FbcQi2/E5X+KlOduC8DGTPBNyIyTUCsQ+FJBxIVO2VRK5eC3sESoNLuBxqgTg95/fnp9ennb4VC74pnMIrQ5YbJIqsWJu4ljektROYbkkuo23zCV2A9OzZ6p8vi2qaf2Wu0T/k1XOnXY4o9d00OEI7xpYYO8ZoH4VYvY36DbQ4S10Tnvdji12jVQpCDa7Ofj+XgcLCIN25mJKLM2fdqx0jVAZbDc26ZPPtevwDt1vMDQ9bWVKY1usG8qq4ZG06k0nKgU8m41ZV+lRvQak8dOVdbdAiwFr62Cq2zUxEcCT2ScZ0p7Up7wa7vuWTFNuqK3fU+K5RobcjMf3kyEP1OgIza8H0+CGkkmalXkcOpozE0xF6I4MUQlq/Thi6YfyF9wk8s8KZW5kc3RyZWFtCmVuZG9iago0IDAgb2JqCjw8L1BhcmVudCAzIDAgUi9Db250ZW50cyAyIDAgUi9UeXBlL1BhZ2UvUmVzb3VyY2VzPDwvUHJvY1NldCBbL1BERiAvVGV4dCAvSW1hZ2VCIC9JbWFnZUMgL0ltYWdlSV0vRm9udDw8L0YxIDEgMCBSPj4+Pi9NZWRpYUJveFswIDAgMjk3IDQyMF0+PgplbmRvYmoKMSAwIG9iago8PC9CYXNlRm9udC9Db3VyaWVyL1R5cGUvRm9udC9FbmNvZGluZy9XaW5BbnNpRW5jb2RpbmcvU3VidHlwZS9UeXBlMT4+CmVuZG9iagozIDAgb2JqCjw8L1R5cGUvUGFnZXMvQ291bnQgMS9LaWRzWzQgMCBSXT4+CmVuZG9iago1IDAgb2JqCjw8L1R5cGUvQ2F0YWxvZy9QYWdlcyAzIDAgUj4+CmVuZG9iago2IDAgb2JqCjw8L1Byb2R1Y2VyKGlUZXh0riA1LjMuNCCpMjAwMC0yMDEyIDFUM1hUIEJWQkEgXChBR1BMLXZlcnNpb25cKSkvTW9kRGF0ZShEOjIwMTQwMjIxMTI1OTUxLTA1JzAwJykvQ3JlYXRpb25EYXRlKEQ6MjAxNDAyMjExMjU5NTEtMDUnMDAnKT4+CmVuZG9iagp4cmVmCjAgNwowMDAwMDAwMDAwIDY1NTM1IGYgCjAwMDAwMDA3NTAgMDAwMDAgbiAKMDAwMDAwMDAxNSAwMDAwMCBuIAowMDAwMDAwODM2IDAwMDAwIG4gCjAwMDAwMDA1OTMgMDAwMDAgbiAKMDAwMDAwMDg4NyAwMDAwMCBuIAowMDAwMDAwOTMyIDAwMDAwIG4gCnRyYWlsZXIKPDwvUm9vdCA1IDAgUi9JRCBbPDEzOGJjNDQ0MGFiMWIwZTY1OTE1OWYzNDk5NGI5OWU2PjxhNjg0N2ViMDlmYWY0MDQ0OWQwZWI1YWQ0YzY5NjczYj5dL0luZm8gNiAwIFIvU2l6ZSA3Pj4KJWlUZXh0LTUuMy40CnN0YXJ0eHJlZgoxMDg1CiUlRU9GCg==\n"));

			Grupo grupo0 = new Grupo("0", datos);

			datos = Arrays.asList();
			datos = Arrays.asList(new Dato("PARA", (String) execution.getVariable("email")),
					new Dato("IDENTIFICACION", "1026262691"), new Dato("TIPODOC", "CC"));

			Grupo grupo1 = new Grupo("1", datos);

			request.setGrupo(Arrays.asList(grupo0, grupo1));

			// log.info(request.toString());
			ObjectMapper mapper = new ObjectMapper();
			try {
				log.info(mapper.writeValueAsString(request));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HttpEntity<RequestEventosDTO> httpEntity = new HttpEntity<>(request, headers);
			ResponseEventosDTO result = restTemplate.postForObject(urlString, httpEntity, ResponseEventosDTO.class);
			log.info(result.toString());
			log.info("Se ha enviado correo al cliente: " + idConsecutivo);
			
		} catch (Exception e) {
			log.error("Error en servicio de Eventos " + " | " + e.getMessage() + " | " + e.getClass() + " | "
					+ e.getLocalizedMessage());
		}

	}
}
