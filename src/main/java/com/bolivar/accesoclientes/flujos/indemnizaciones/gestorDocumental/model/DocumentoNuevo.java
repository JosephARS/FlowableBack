package com.bolivar.accesoclientes.flujos.indemnizaciones.gestorDocumental.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DocumentoNuevo {

	//String nombreDocumento;
	List<MultipartFile> documentoBin;
	
}
