/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadNoExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.FotoPerfilUserResponse;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.services.FotoPerfilService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

/**
 *
 * @author pablo
 */
@Path("usuarios/foto")
public class UsuarioFotoResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerFotoPerfi(@PathParam("id") String id) {

        try {
            FotoPerfilService fotoService = new FotoPerfilService();

            String fotoBase64 = fotoService.obtenerFotoPerfil(id);

            return Response.ok(new FotoPerfilUserResponse(fotoBase64)).build();

        } catch (EntidadNoExistenteException ex) {

            //Indica que no se encontro la entidad
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();

        } catch (FormatoInvalidoException ex) {
            //Indica que la solicitud no fue procesada
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        }
    }
}
