/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.anunciante;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.AnuncioRegistradoDTOResponse;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.CambiarEstadoRequest;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.services.AnunciosRegistradosClienteService;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.services.AnunciosRegistradosService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
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
@Path("anunciante/anuncios")
public class AnuncianteResource {

    //Endpoint que ayuda a retornar los anuncios registrados en el sistema por el anunciante
    @GET
    @Path("/limit/{limite}/offset/{inicio}/usuario/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response anunciosRegistrados(
            @PathParam("limite") String limite,
            @PathParam("idUsuario") String idUsuario,
            @PathParam("inicio") String inicio) {

        AnunciosRegistradosClienteService anunciosRegistradosClienteService = new AnunciosRegistradosClienteService();

        try {
            List<AnuncioRegistradoDTOResponse> anunciosRegistrados = anunciosRegistradosClienteService.obtenerAnunciosRegistradosCliente(limite, inicio, idUsuario);

            return Response.ok(anunciosRegistrados).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Endpoint que permite obtener la cantidad total de anuncios comprados por el anunciante
    @GET
    @Path("/cantidad/usuario/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cantidadAnunciosComprados(@PathParam("idUsuario") String idUsuario) {

        AnunciosRegistradosClienteService anunciosRegistradosClienteService = new AnunciosRegistradosClienteService();

        try {

            CantidadReportesDTO cantidadAnuncios = anunciosRegistradosClienteService.cantidadAnunciosComprados(idUsuario);
            return Response.ok(cantidadAnuncios).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }
    }

    //Endpoint que permite poder cambiar el estado de los anuncios
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cambiarEstado(CambiarEstadoRequest request) {

        AnunciosRegistradosClienteService anunciosRegistradosClienteService = new AnunciosRegistradosClienteService();

        try {

            if (anunciosRegistradosClienteService.cambiarEstadoAnuncio(request)) {
                return Response.ok().build();
            } else {
                throw new ErrorInesperadoException("No se ha podido cambiar el estado del anuncio");
            }

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
