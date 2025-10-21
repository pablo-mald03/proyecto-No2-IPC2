/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadNoExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.LoginRequest;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.UserLoggedDTO;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.UserLoggedResponse;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.services.LoginService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

/**
 *
 * @author pablo
 */
//Endpoint que permite ejecutar el login del usuario
@Path("login")
public class LoginResource {

    @Context
    UriInfo uriInfo;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generarLogin(LoginRequest loginRequest) {

        LoginService loginAutorizacion = new LoginService();

        try {
            UserLoggedDTO usuarioResponse = loginAutorizacion.autenticarUsuario(loginRequest);
            return Response.ok(new UserLoggedResponse(usuarioResponse)).build();

        } catch (EntidadNoExistenteException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

}
