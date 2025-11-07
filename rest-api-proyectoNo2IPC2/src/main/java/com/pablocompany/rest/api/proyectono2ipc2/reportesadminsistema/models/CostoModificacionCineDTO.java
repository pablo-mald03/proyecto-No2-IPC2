/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models;

import java.time.LocalDate;

/**
 *
 * @author pablo
 */
//Clase delegada para poder ver el costo de modificacion de cada cine
public class CostoModificacionCineDTO {

    private double costo;
    private LocalDate fechaModificacion;

    public CostoModificacionCineDTO(double costo, LocalDate fechaModificacion) {
        this.costo = costo;
        this.fechaModificacion = fechaModificacion;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    
    
    
    

}
