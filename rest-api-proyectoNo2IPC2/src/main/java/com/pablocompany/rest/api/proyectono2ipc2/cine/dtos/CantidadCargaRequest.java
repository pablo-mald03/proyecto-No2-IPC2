/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine.dtos;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con los limites para poder mostar dinamicamente los registros
public class CantidadCargaRequest {

    private int offset;
    private int limit;

    //Constructor utilizado simplemente para poder instanciar el request vacio 
    public CantidadCargaRequest(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    //Metodo delegado para validar el request de datos
    public boolean validarRequest() throws FormatoInvalidoException{
        
        if(limit < 0){
            throw  new FormatoInvalidoException("No se puede enviar un limite menor que cero");
        }
        
        if( offset< 0){
            throw  new FormatoInvalidoException("No se puede enviar un offset menor que cero");
        }
        
        return true; 
        
    }
}
