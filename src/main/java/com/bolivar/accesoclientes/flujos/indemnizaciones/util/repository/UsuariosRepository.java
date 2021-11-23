package com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bolivar.accesoclientes.flujos.indemnizaciones.asignarUsuarioTarea.model.Usuario;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuario, Integer>{
	
	@Procedure("p_obtener_usuario")
	String P_OBTENER_USUARIO(String id_Proceso, String id_tarea);

	@Procedure("p_actualizar_usuario_asignado")
	Integer P_ACTUALIZAR_USUARIO_ASIGNADO(String id_proceso, String usuario_asignado, String rol); //Al enviar Rol='Responsable' se actualiza el responsable asignado al caso, cualquier otro valor, no lo modifica. 
	
	@Procedure("p_tarea_cerrada")
	Integer P_TAREA_CERRADA(String usuario_asignado, String tipo_cierre);
	
	@Procedure("p_reasignar_usuario")
	Integer P_REASIGNAR_USUARIO(String id_proceso, String usuario_asignado);
	
	@Query(value = "call p_listar_usuarios(:idTareaDefinicion)",nativeQuery = true)
	List<Usuario> P_LISTAR_USUARIOS(@Param("idTareaDefinicion") String idTareaDefinicion);
	
	@Procedure("p_cambiar_estado_usuario")
	Integer P_CAMBIAR_ESTADO_USUARIO(Integer id_usuario, Integer estado);
}
