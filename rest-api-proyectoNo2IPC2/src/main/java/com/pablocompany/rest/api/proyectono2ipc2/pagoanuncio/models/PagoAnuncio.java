/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.pagoanuncio.models;

import java.time.LocalDate;

/**
 *
 * @author pablo
 */
//Clase que representa el objeto de pago de anuncio. Delegada para operar con los datos
public class PagoAnuncio {
    
    private double monto;
    private LocalDate fechaPago;
    private String idUsuario;

    public PagoAnuncio(double monto, LocalDate fechaPago, String idUsuario) {
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.idUsuario = idUsuario;
    }

    
    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
    
}
