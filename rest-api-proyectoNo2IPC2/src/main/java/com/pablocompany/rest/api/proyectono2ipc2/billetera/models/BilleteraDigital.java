/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.billetera.models;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con la billetera digital y el saldo del usuario 
public class BilleteraDigital {
    
    private double saldo;
    private String idUsuario;

    public BilleteraDigital(double saldo, String idUsuario) {
        this.saldo = saldo;
        this.idUsuario = idUsuario;
    }

    
    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
    
    
    
}
