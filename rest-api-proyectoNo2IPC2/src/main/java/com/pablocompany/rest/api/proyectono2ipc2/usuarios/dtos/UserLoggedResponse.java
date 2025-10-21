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
//Clase que permite generar el response cuando se ejecuto un login
public class UserLoggedResponse {
    
    private String id;
    private TipoUsuarioEnum rol;
    
    public UserLoggedResponse(UserLoggedDTO usuario){
        this.id = usuario.getId();
        this.rol = usuario.getRol();
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
