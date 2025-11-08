/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.models;

/**
 *
 * @author pablo
 */
//Enum que represente las tarifas que ofrece la base de datos para comprar anuncios
public enum VigenciaAnuncioEnum {
    
    UN_DIA(1),
    TRES_DIAS(2),
    UNA_SEMANA(3), 
    DOS_SEMANAS(4);
    
    private int contexto; 

    private VigenciaAnuncioEnum(int contexto) {
        this.contexto = contexto;
    }

    public int getContexto() {
        return contexto;
    }
    
    
}
