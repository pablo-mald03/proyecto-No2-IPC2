/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author pablo
 */
@Path("usuarios")
public class UsuarioResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearUsuario() {
        return Response.status(Response.Status.CREATED).build();
    }

}
