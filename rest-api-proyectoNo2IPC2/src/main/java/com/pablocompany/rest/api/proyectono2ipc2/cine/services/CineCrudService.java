/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine.services;

import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CineRequest;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.Cine;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con el crud de los cines 
public class CineCrudService {
    
    //Metodo utilizado para crear cines
    public boolean crearCine(CineRequest request) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException{
     
        
        Cine nuevoCine = extraerDatos(request);
        
        
        return true;
    }
    
    //Metodo delegado para poder extraer los datos del cine request 
    private Cine extraerDatos(CineRequest request){
        
       
        
        return null;
        
    }
    
}
