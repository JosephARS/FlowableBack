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
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.ObjCodigoValor;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.Tarea;

@Repository
public interface InfoGeneralProcesoRepository extends JpaRepository<InfoGeneralProceso, Long>{

	
	@Procedure("p_crear_nuevo_caso")
	String P_CREAR_NUEVO_CASO(String id_Proceso, String creacion_variables);
	
	@Procedure("p_anular_caso")
	Integer P_ANULAR_CASO(String id_Proceso, String datos_anulacion);
	
	@Procedure("p_actualizar_estado_proceso")
	Integer P_ACTUALIZAR_ESTADO(String id_Proceso, String estado);
	
	@Procedure("p_finalizar_proceso")
	Integer P_FINALIZAR_PROCESO(String id_Proceso, String estadoFinal);
	
	@Query(value = "call p_buscar_proceso(:parametroBusqueda, :valorBusqueda, :itemPorPagina, :primerItem)",nativeQuery = true)
	List<InfoGeneralProceso> P_BUSCAR_PROCESO(@Param("parametroBusqueda") String parametroBusqueda,@Param("valorBusqueda")  String valorBusqueda,@Param("itemPorPagina")  Integer itemPorPagina,@Param("primerItem")  Integer primerItem );
	
	List<InfoGeneralProceso> findByIdentificacionAsegurado(String identificacionAsegurado);
	
	List<InfoGeneralProceso> findByIdProcesoIn(List<String> idProceso);
	
	Optional<InfoGeneralProceso> findByIdProceso(String idProceso);
	
/******PROCEDURES COMPLETAR TAREAS ******/
	@Procedure("p_actualizar_secc_siniestro")
	Integer actualizarValorReserva(String id_Proceso, String siniestro);
	
	@Procedure("p_actualizar_secc_infoProceso")
	Integer conceptoAnalisisCaso(String id_Proceso, String infoGeneral);
	
	@Procedure("p_actualizar_secc_canalAtencion")
	Integer creacionCasoStellent(String id_Proceso, String canalAtencion);

	@Procedure("p_actualizar_secc_ajustador")
	Integer asignarAjustador(String id_Proceso, String ajustador);
	
	@Procedure("p_actualizar_secc_objecion")
	Integer explicacionCasoObjetado(String idProceso, String objecion);
	
	@Procedure("p_actualizar_secc_pago")
	Integer validacionLiquidacion(String idProceso, String pago);
	
		
	
	
/******QUERYS PARA ACTUALIZAR CAMPOS ESPECIFICOS ******/	
	@Modifying
    @Query(value = "update info_general_proceso set documento = JSON_INSERT(documento, \"$.pago\", JSON_OBJECT(\"valorPago\", :valorPago)) where idProceso = :idProceso" , nativeQuery = true)
    int updateValorByIdProceso(@Param("idProceso") String idProceso, @Param ("valorPago") Long valor);

	@Transactional
	@Modifying
    @Query(value = "update info_general_proceso set documento = JSON_SET(documento, \"$.siniestro.valorReserva\", :valorReserva, \"$.siniestro.tipoEvento\", :tipoEvento) where idProceso = :idProceso" , nativeQuery = true)
    int updateValorReserva(@Param("idProceso") String idProceso, @Param ("valorReserva") Long valorReserva, @Param("tipoEvento") String tipoEvento);
	
	@Transactional
	@Modifying
    @Query(value = "update info_general_proceso set documento = JSON_SET(documento, \"$.siniestro.numeroSiniestro\", :numeroSiniestro) where idProceso = :idProceso" , nativeQuery = true)
    int updateNumeroSiniestro(@Param("idProceso") String idProceso, @Param ("numeroSiniestro") Long numeroSiniestro);
		
	@Procedure("p_actualizar_motor_def")
	Integer P_ACTUALIZAR_MOTOR_DEF(String id_Proceso, String infoGeneral);
	
	@Procedure("p_ingresar_analisis")
	Integer P_INGRESAR_ANALISIS(String id_Proceso, String infoGeneral);
	

	
}
