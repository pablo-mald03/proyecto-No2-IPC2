/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.services;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.AnuncioDTORequest;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;

/**
 *
 * @author pablo
 */
//Clase que permite gestionar todoas las interacciones DEL ANUNCIANTE 
public class AnunciosCrudService {
    
    
    //Metodo que permite al usuario comprar un nuevo anuncio
    public boolean comprarAnuncio(AnuncioDTORequest request) throws FormatoInvalidoException,DatosNoEncontradosException, ErrorInesperadoException{
        return true;
    }
    
    
    
}
