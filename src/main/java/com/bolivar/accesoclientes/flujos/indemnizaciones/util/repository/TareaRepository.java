package com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Tarea;


@Repository
public interface TareaRepository extends JpaRepository<Tarea, Integer>{

	@Query(value = "call p_obtener_tiempo_atencion()", nativeQuery = true)
	List<Tarea> P_OBTENER_TIEMPO_ATENCION();
	
}
