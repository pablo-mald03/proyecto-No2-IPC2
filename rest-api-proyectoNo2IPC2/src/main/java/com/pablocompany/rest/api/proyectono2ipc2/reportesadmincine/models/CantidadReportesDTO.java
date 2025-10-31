/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models;

/**
 *
 * @author pablo
 */
//Clase delegada para poder retornar la instancia de cantidad de reportes
public class CantidadReportesDTO {
    
    private int cantidad;

    public CantidadReportesDTO(int cantidad) {
        this.cantidad = cantidad;
    }
    
   
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    
    
}
