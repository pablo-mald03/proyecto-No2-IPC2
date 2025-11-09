/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine.models;

/**
 *
 * @author pablo
 */
//Clase delegada para poderla enviar con datos a backend o a frontend sobre el nombre y el id de cines asociados
public class CineAsociadoDTO {

    private String codigo;

    public CineAsociadoDTO() {
    }

    public CineAsociadoDTO(String codigo) {
        this.codigo = codigo;

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
