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
import java.io.InputStream;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author pablo
 */
@Path("administrador/usuarios/sistema")
public class AdministradorSistemaResource {

    //Endpont que comunica al administrador de sistema para poder crear administradores de sistema
    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response crearAdministrador(
            @FormDataParam("id") String id,
            @FormDataParam("correo") String correo,
            @FormDataParam("nombre") String nombre,
            @FormDataParam("password") String password,
            @FormDataParam("confirmPassword") String confirmPassword,
            @FormDataParam("telefono") String telefono,
            @FormDataParam("pais") String pais,
            @FormDataParam("identificacion") String identificacion,
            @FormDataParam("codigoRol") String codigoRol,
            @FormDataParam("foto") InputStream foto,
            @FormDataParam("foto") FormDataContentDisposition fotoDetalle
            
            
            
            
    ) {
        
        
        

        return Response.ok("").build();
    }

    //Endpoint que ayuda a obtener el listado general de administradores de sistema
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodosAdmins() {

        return Response.ok("").build();
    }

}
