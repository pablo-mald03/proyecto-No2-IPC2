/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.cines;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.CantidadRegistrosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CineRequest;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.Cine;
import com.pablocompany.rest.api.proyectono2ipc2.cine.services.CineCrudService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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
@Path("cines")
public class CineResource {

    //Endpoint que permite crear nuevos cines
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearCine(CineRequest request) {

        CineCrudService cineCrudService = new CineCrudService();
        try {

            if (cineCrudService.crearCine(request)) {
                return Response.ok().build();
            } else {
                throw new ErrorInesperadoException("No se ha podido crear el nuevo cine");
            }

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite obtener el listado de cines asociados en el sistema
    @GET
    @Path("/limit/{limite}/offset/{inicio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodosCines(
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        CineCrudService cineCrudService = new CineCrudService();

        try {
            List<Cine> reporteSalasGustadasDto = cineCrudService.obtenerCinesAsociados(limite, inicio);

            return Response.ok(reporteSalasGustadasDto).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite obtener la cantidad de cines asociados en el sistema
    @GET
    @Path("/cantidad")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCantidadCines() {

        CineCrudService cineCrudService = new CineCrudService();

        try {
            CantidadRegistrosDTO cantidadRegistros = cineCrudService.obtenerCantidadCines();
            return Response.ok(cantidadRegistros).build();

        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
