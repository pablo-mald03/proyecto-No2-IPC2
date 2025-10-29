/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.excepciones;

/**
 *
 * @author pablo
 */
//Excepcion delegada para poder notificar errores saltados en la base de datos
public class ErrorInesperadoException extends  Exception{

    public ErrorInesperadoException() {
    }

    public ErrorInesperadoException(String message) {
        super(message);
    }
    
    
    
}
