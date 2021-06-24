package com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model;

import org.flowable.variable.service.impl.types.SerializableType;

public class DocumentoType extends SerializableType {
	
    public static final String TYPE_NAME = "Documento";
    
    private final String nombre;
    private final String urlDescarga;
	
	public DocumentoType(String nombre,String urlDescarga) {
		this.nombre = nombre;
		this.urlDescarga = urlDescarga;
		//super();
	}
	
    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }
    
    @Override
    public boolean isAbleToStore(Object value) {
        if (value == null) {
            return false;
        }
        else {
			return true;
		}
    }

}
