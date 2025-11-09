/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models;

import java.time.LocalDate;

/**
 *
 * @author pablo
 */
//Clase delegada para poder procesar el objeto de pago de ocultacion de anuncios
public class PagoOcultacionAnuncios {
    
    private double monto;
    private String codigoCine;
    private LocalDate fechaPago;

    public PagoOcultacionAnuncios(double monto, String codigoCine, LocalDate fechaPago) {
        this.monto = monto;
        this.codigoCine = codigoCine;
        this.fechaPago = fechaPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
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
