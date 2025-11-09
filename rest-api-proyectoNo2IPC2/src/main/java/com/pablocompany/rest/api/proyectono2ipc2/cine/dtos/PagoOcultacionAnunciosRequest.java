/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine.dtos;

import java.time.LocalDate;

/**
 *
 * @author pablo
 */

//Clase delegada para poder recibir los datos requeridos para realizar una trancsaccion en el cine 
public class PagoOcultacionAnunciosRequest {
    
    private String monto;
    private String codigoCine;
    private LocalDate fechaPago;

    public PagoOcultacionAnunciosRequest() {
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getCodigoCine() {
        return codigoCine;
    }

    public void setCodigoCine(String codigoCine) {
        this.codigoCine = codigoCine;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }
    
    
}
