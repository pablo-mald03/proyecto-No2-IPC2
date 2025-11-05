/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.administradorsistema;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.CantidadRegistrosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.services.UsuarioAdministradoresCineService;
import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.services.UsuariosAdministradoresSistemaService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.UsuarioDatosResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pablo
 */
@Path("administrador/usuarios/cine")
public class UsuarioAdminCineResource {

    //Endpont que comunica al administrador de sistema para poder crear administradores de cine
    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response crearAdministrador() {

        return Response.ok("").build();
    }

    //Endpoint que ayuda a obtener el listado general de administradores de cine
    @GET
    @Path("/limit/{limite}/offset/{tope}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodosAdmins(
            @PathParam("limite") String limite,
            @PathParam("tope") String inicio) {

        UsuarioAdministradoresCineService usuariosAdministradoresCine = new UsuarioAdministradoresCineService();

        try {
            List<UsuarioDatosResponse> usuariosResponse = usuariosAdministradoresCine.obtenerAdministradores(limite, inicio);

            return Response.ok(usuariosResponse).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }
    }

    //Endpoint que obtene la cantidad de administradores de cine
    @GET
    @Path("/cantidad")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCantidadRegistros() {

        UsuarioAdministradoresCineService usuariosAdministradoresCine = new UsuarioAdministradoresCineService();

        try {
            CantidadRegistrosDTO cantidadReportes = usuariosAdministradoresCine.cantidadRegistros();
            return Response.ok(cantidadReportes).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }
    }

}
