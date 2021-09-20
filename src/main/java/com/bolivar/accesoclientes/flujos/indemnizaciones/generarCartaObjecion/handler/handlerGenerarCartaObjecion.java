package com.bolivar.accesoclientes.flujos.indemnizaciones.generarCartaObjecion.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Slf4j
@Component
public class handlerGenerarCartaObjecion implements JavaDelegate{
	 
	@Override
	public void execute(DelegateExecution execution) {
			
		Map<String, String> datasourceMap = new HashMap<>();
		
		List<Map<String, String>> variablesCliente = new ArrayList<Map<String, String>>();
					
		datasourceMap.put("Nombre", "Joseph Rodriguez");
		datasourceMap.put("Producto", execution.getProcessInstanceId().toString() );
		
		variablesCliente.add(datasourceMap);
		
		
		try {

			JasperReport	report = (JasperReport) JRLoader.loadObject(getClass().getResource("/Simple_Blue.jasper"));
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(variablesCliente);
			JasperPrint	jprint = JasperFillManager.fillReport(report, null, dataSource);
			JasperExportManager.exportReportToPdfFile(jprint, "C:\\PDFPRUBEA\\carta2.pdf");
			
			log.info("Se ha generado carta de objecion");

		} catch (Exception e) {
			log.error("No se ha podido generar la carta de objecion" + e.getMessage() + "|" +e.getCause() + "|" + e.getStackTrace()[0]);
		}
		
		

	}
}