/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.excepciones;

/**
 *
 * @author pablo
 */
//Clase que indica que cierta entidad ya existe
public class EntidadExistenteException extends  Exception {

    public EntidadExistenteException() {
    }

    public EntidadExistenteException(String message) {
        super(message);
    }
    
}
