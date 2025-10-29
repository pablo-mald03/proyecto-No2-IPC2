/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.excepciones;

/**
 *
 * @author pablo
 */
//Clase delegada para retornar cuando los datos no fueron encontrados
public class DatosNoEncontradosException extends Exception{

    public DatosNoEncontradosException() {
    }

    public DatosNoEncontradosException(String message) {
        super(message);
    }
    
}
