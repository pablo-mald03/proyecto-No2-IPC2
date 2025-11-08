/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.excepciones;

/**
 *
 * @author pablo
 */
//Clase delegada para mostrar la excepcion cuando no se tiene el saldo suficiente
public class TransaccionInvalidaException extends Exception {

    public TransaccionInvalidaException() {
    }

    public TransaccionInvalidaException(String message) {
        super(message);
    }

}
