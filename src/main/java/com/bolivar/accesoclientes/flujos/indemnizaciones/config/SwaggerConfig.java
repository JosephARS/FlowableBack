package com.bolivar.accesoclientes.flujos.indemnizaciones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class SwaggerConfig {

		@Bean
		public Docket apiDocket() {
			return new Docket(DocumentationType.SWAGGER_2)
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.bolivar.accesoclientes.flujos.indemnizaciones"))
					.paths(PathSelectors.any())
					.build();
					//.apiInfo(getApiInfo());
		}
		
//		private ApiInfo getApiInfo() {
//			return new ApiInfo(
//					"Flujo Indemnizaciones Generales API",
//					"Flujo de trabajo Indemnizaciones generales",
//					"1.0",
//					"http://codmind.com/terms",
//					new Contact("Codmind", "https://codmind.com", "apis@codmind.com"),
//					"LICENSE",
//					"LICENSE URL",
//					Collections.emptyList()
//					);
//		}
}
