package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.NuevoCasobd;

@Repository
public interface NuevoCasoRepository extends JpaRepository<NuevoCasobd, Integer> {

	@Procedure("p_crear_nuevo_caso")
	String P_CREAR_NUEVO_CASO(String id_Proceso, String creacion_variables);
	
}
