package com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.InfoGeneralProceso;

@Repository
public interface InfoGeneralProcesoRepository extends JpaRepository<InfoGeneralProceso, Long>{

	
	@Procedure("p_crear_nuevo_caso")
	String P_CREAR_NUEVO_CASO(String id_Proceso, String creacion_variables);
	
	@Procedure("p_anular_caso")
	Integer P_ANULAR_CASO(String id_Proceso, String datos_anulacion);
	
	List<InfoGeneralProceso> findByIdentificacionAsegurado(String identificacionAsegurado);
	
	List<InfoGeneralProceso> findByIdProcesoIn(List<String> idProceso);
	
	Optional<InfoGeneralProceso> findByIdProceso(String idProceso);
	
	@Modifying
    @Query(value = "update info_general_proceso set documento = JSON_INSERT(documento, \"$.pago\", JSON_OBJECT(\"valorPago\", :valorPago)) where idProceso = :idProceso" , nativeQuery = true)
    int updateValorByIdProceso(@Param("idProceso") String idProceso, @Param ("valorPago") Long valor);

	@Transactional
	@Modifying
    @Query(value = "update info_general_proceso set documento = JSON_SET(documento, \"$.siniestro.valorReserva\", :valorReserva, \"$.siniestro.tipoEvento\", :tipoEvento) where idProceso = :idProceso" , nativeQuery = true)
    int updateValorReserva(@Param("idProceso") String idProceso, @Param ("valorReserva") Long valorReserva, @Param("tipoEvento") String tipoEvento);
}
