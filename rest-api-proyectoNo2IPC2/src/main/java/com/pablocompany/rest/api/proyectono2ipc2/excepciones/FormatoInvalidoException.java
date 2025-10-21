/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.excepciones;

/**
 *
 * @author pablo
 */
//Excepcion que se encarga de reportar si una entrada es invalida
public class FormatoInvalidoException extends Exception{

    public FormatoInvalidoException() {
    }

    public FormatoInvalidoException(String message) {
        super(message);
    }
    
    
    
}
