/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.billeteracine.models;

/**
 *
 * @author pablo
 */
//Clase delegada para poder manejar el objeto de la billetera digital del cine
public class BilleteraCine {
    
    
    private double saldo;
    private String codigoCine;

    public BilleteraCine(double saldo, String codigoCine) {
        this.saldo = saldo;
        this.codigoCine = codigoCine;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getCodigoCine() {
        return codigoCine;
    }

    public void setCodigoCine(String codigoCine) {
        this.codigoCine = codigoCine;
    }
    
}
