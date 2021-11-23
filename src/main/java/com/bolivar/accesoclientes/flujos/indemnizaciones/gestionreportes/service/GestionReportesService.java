package com.bolivar.accesoclientes.flujos.indemnizaciones.gestionreportes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.ResponseWS;
import com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model.TipoRespuesta;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestionreportes.DAO.gestionReportesDAO;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestionreportes.model.Detalle;
import com.bolivar.accesoclientes.flujos.indemnizaciones.gestionreportes.model.ReporteTareasActivas;
import com.bolivar.accesoclientes.flujos.indemnizaciones.util.model.ObjCodigoValor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GestionReportesService implements gestionReportesDAO {

	TaskService taskService;

	@Override
	public ResponseWS reporTareasActivas() {
		
		ResponseWS oResponseWS = new ResponseWS();
		
		try {
			List<Task> tareas = taskService.createTaskQuery().list();

			List<ObjCodigoValor> conteoTareas = new ArrayList<ObjCodigoValor>();
			Map<String, Object> map = new HashMap<String, Object>();
			

			
			System.out.println("Tareas " + tareas.size() + tareas);
		
			tareas.forEach(tarea -> {
				ObjCodigoValor obj = new ObjCodigoValor();
				obj.setCodigo(tarea.getAssignee());
				obj.setValor(tarea.getName());
				conteoTareas.add(obj);
			});

			Map<Object, Map<Object, Long>> couterMap = tareas.stream()
					.collect(Collectors.groupingBy(s -> s.getName(), Collectors.groupingBy(x -> x.getAssignee(), Collectors.counting()))); // Agrupa y cuentas la
			
			System.out.println("conteoTareas" + conteoTareas);// cantidad de tareas
			System.out.println("CounterMap" + couterMap);

			Map<String, Object> mapaObjeto = new HashMap<String, Object>();
			List<Object> listaTareasCantidad = new ArrayList<Object>();

			for (Entry<Object, Map<Object, Long>> counter : couterMap.entrySet()) {


				Map<Object, Long>  map1 = new HashMap<Object, Long>(counter.getValue());

				ReporteTareasActivas oReporteTareasActivas = new ReporteTareasActivas();

				ObjCodigoValor oTareasCantidad = new ObjCodigoValor();
				List<Object> listaUsuarios = counter.getValue().values().stream().collect(Collectors.toList());
				System.out.println("Pruebaaaaaaaaaaaa " + counter.getKey() + counter.getValue());
//				mapaObjeto.put("tarea", counter.getKey().toString());
				
				oReporteTareasActivas.setTarea(counter.getKey().toString());
				System.out.println("Key: " + counter.getKey().toString());
				
				   List<Detalle> listaDetalles = new  ArrayList<>();
				
				   for (Entry<Object, Long> entry : map1.entrySet()) {
						Detalle oDetalle = new Detalle();
					   Object key = entry.getKey();
					   Long value = entry.getValue();
					   oDetalle.setUsuario(key.toString());
					   oDetalle.setCantidad(value);

					   listaDetalles.add(oDetalle);

					}
				
				   oReporteTareasActivas.setDetalle(listaDetalles);

				
//				oTareasCantidad.setCodigo(counter.getKey().toString());
//				oTareasCantidad.setValor(counter.getValue().toString());
				listaTareasCantidad.add(oReporteTareasActivas);
				System.out.println("Pruebaaaaaaaaaaaa2 " + listaUsuarios);
			}

			oResponseWS.setListaResultado(listaTareasCantidad);
			oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);

		} catch (Exception e) {
			oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
			oResponseWS.setMensaje("Error obteniendo cantidad tareas ");
			log.error("Error obteniendo cantidad tareas " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
		}

		return oResponseWS;
	}

}
