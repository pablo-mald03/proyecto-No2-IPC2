/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.dtos;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con el request de consultas dinamicas de administradores de sistema 
public class AdministradoresRequest {

    private int offset;
    private int limit;

    //Constructor utilizado simplemente para poder instanciar el request vacio 
    public AdministradoresRequest(int offset, int limit) {
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

}
