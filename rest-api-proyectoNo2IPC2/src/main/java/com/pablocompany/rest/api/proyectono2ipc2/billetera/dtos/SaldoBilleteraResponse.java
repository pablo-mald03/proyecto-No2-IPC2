/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.billetera.dtos;

import com.pablocompany.rest.api.proyectono2ipc2.billetera.models.SaldoBilleteraDTO;

/**
 *
 * @author pablo
 */
//Clase delegada para poder retornar la cantidad de saldo actual del usuario
public class SaldoBilleteraResponse {

    private double saldo;

    public SaldoBilleteraResponse(SaldoBilleteraDTO billetera) {
        this.saldo = billetera.getSaldo();
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

}
