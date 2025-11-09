/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.billeteracine.dtos;

/**
 *
 * @author pablo
 */
//Clase encargada de procesar el request para recargar la billetera
public class BilleteraCineRequest {

    private String codigoCine;
    private String saldo;
    private String nombre;

    public BilleteraCineRequest() {
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getCodigoCine() {
        return codigoCine;
    }

    public void setCodigoCine(String codigoCine) {
        this.codigoCine = codigoCine;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
