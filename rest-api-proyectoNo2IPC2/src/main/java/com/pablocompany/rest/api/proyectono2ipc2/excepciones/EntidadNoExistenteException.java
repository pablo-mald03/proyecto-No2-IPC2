/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.excepciones;

/**
 *
 * @author pablo
 */
//Clase encargada de reportar si la entidad no existe
public class EntidadNoExistenteException extends  Exception{

    public EntidadNoExistenteException() {
    }

    public EntidadNoExistenteException(String message) {
        super(message);
    }
    
}
