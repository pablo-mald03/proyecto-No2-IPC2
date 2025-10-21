/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos;

import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.TipoUsuarioEnum;

/**
 *
 * @author pablo
 */
//Retorna los atributos necesarios para hacer login
public class UserLoggedDTO {

    private String id;
    private TipoUsuarioEnum rol;

    public UserLoggedDTO(String id, TipoUsuarioEnum rol) {
        this.id = id;
        this.rol = rol;
    }
   

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TipoUsuarioEnum getRol() {
        return rol;
    }

    public void setRol(TipoUsuarioEnum rol) {
        this.rol = rol;
    }

}
