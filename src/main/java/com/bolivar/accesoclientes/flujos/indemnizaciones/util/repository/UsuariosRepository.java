package com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.bolivar.accesoclientes.flujos.indemnizaciones.asignarUsuarioTarea.model.Usuario;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuario, Integer>{
	
	@Procedure("p_obtener_usuario")
	String P_OBTENER_USUARIO(String id_Proceso, String id_tarea);

	@Procedure("p_actualizar_usuario_asignado")
	Integer P_ACTUALIZAR_USUARIO_ASIGNADO(String id_proceso, String usuario_asignado);
}
