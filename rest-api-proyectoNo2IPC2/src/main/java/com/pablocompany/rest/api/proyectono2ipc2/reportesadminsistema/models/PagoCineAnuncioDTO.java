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
//Clase delegada para poder operar con el objeto de pago de cines en los anuncios 
public class PagoCineAnuncioDTO {

    private String idUsuario;
    private double monto;
    private LocalDate fechaPago;

    public PagoCineAnuncioDTO(String idUsuario, double monto, LocalDate fechaPago) {
        this.idUsuario = idUsuario;
        this.monto = monto;
        this.fechaPago = fechaPago;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
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

}
