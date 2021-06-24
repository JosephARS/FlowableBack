package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.Consecutivos;

@Repository
public interface ConsecutivosRepository extends JpaRepository<Consecutivos, String> {

	@Procedure("p_crear_consecutivo")
	String P_CREAR_CONSECUTIVO(String id_Flowable);
	

}
