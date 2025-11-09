/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.billeteracine.models;

/**
 *
 * @author pablo
 */
//Clase delegada para poder mostrar la billetera de cine asociada a los administradores de cine 
public class BilleteraCineDTO {
    
    private String codigoCine;
    private double saldo;
    private String nombre;

    public BilleteraCineDTO(String codigoCine, double saldo, String nombre) {
        this.codigoCine = codigoCine;
        this.saldo = saldo;
        this.nombre = nombre;
    }
    
    public String getCodigoCine() {
        return codigoCine;
    }

    public void setCodigoCine(String codigoCine) {
        this.codigoCine = codigoCine;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
    
}
