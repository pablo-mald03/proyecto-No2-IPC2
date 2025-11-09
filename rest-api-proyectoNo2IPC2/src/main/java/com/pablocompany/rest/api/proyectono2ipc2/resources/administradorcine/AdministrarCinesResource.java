/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.administradorcine;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.CantidadRegistrosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.PagoOcultacionAnunciosRequest;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.CineDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.services.CineCrudService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.TransaccionInvalidaException;
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
@Path("administrador/cine/gestion")
public class AdministrarCinesResource {

    //Enpoint que permite obtener el listado de cines asociados en los que se encuentra el administrador de cine 
    @GET
    @Path("/limit/{limite}/offset/{inicio}/id/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodosCinesAsociados(
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio,
            @PathParam("idUsuario") String idUsuario) {

        CineCrudService cineCrudService = new CineCrudService();

        try {
            List<CineDTO> listadoCines = cineCrudService.obtenerCantidadAsociadaAdmin(limite, inicio, idUsuario);

            return Response.ok(listadoCines).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite obtener la cantidad de cines que tiene asociados el administrador de cine
    @GET
    @Path("/cantidad/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCantidadCines(@PathParam("idUsuario") String idUsuario) {

        CineCrudService cineCrudService = new CineCrudService();

        try {
            CantidadRegistrosDTO cantidadRegistros = cineCrudService.obtenerCantidadAsignacionesAdmin(idUsuario);
            return Response.ok(cantidadRegistros).build();

        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite obtener la referencia de un cine
    @GET
    @Path("/cine/codigo/{codigoCine}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCinePorCodigo(@PathParam("codigoCine") String codigoCine) {

        CineCrudService cineCrudService = new CineCrudService();

        try {
            CineDTO cineDto = cineCrudService.obtenerCineCodigo(codigoCine);
            return Response.ok(cineDto).build();

        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite obtener la cantidad de cines que tiene asociados el administrador de cine
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response obtenerCinePorCodigo(PagoOcultacionAnunciosRequest request) {

        CineCrudService cineCrudService = new CineCrudService();

        try {
            if (cineCrudService.ejcutarTransaccionOcultacion(request)) {
                return Response.ok().build();
            }else{
                throw new TransaccionInvalidaException("No se ha podido ejecutar el pago de ocultacion de anuncios");
            }

        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (TransaccionInvalidaException ex) {
            return Response.status(Response.Status.PAYMENT_REQUIRED).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
