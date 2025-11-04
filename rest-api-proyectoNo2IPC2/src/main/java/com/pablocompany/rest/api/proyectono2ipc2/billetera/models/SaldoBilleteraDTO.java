/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.billetera.models;

/**
 *
 * @author pablo
 */
//Clase delegada para poder retornar el saldo de la billetera actual del usuario
public class SaldoBilleteraDTO {

    private double saldo;

    public SaldoBilleteraDTO(double saldo) {
        this.saldo = saldo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

}
