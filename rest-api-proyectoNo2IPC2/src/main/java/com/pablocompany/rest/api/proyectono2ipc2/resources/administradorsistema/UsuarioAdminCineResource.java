/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.administradorsistema;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author pablo
 */
@Path("administrador/usuarios/cine")
public class UsuarioAdminCineResource {
    
    
    //Endpont que comunica al administrador de sistema para poder crear administradores de cine
    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response crearAdministrador(){
        
        
        
        return Response.ok("").build();
    }
    
    
    //Endpoint que ayuda a obtener el listado general de administradores de cine
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodosAdmins(){
        
        
        
        return Response.ok("").build();
    }
    
}


