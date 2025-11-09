/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.principal;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.AnuncioRegistradoDTOResponse;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.AnunciosPublicidadDTO;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.services.AnunciosRegistradosService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.principal.ConsultarPublicidadService;
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
@Path("principal/publicidad")
public class AnunciosPublicidadResource {

    //Endpoint que ayuda a retornar los anuncios registrados en el sistema para publicidad
    @GET
    @Path("{numero}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response anunciosRegistrados(
            @PathParam("numero") String numero) {

        ConsultarPublicidadService consultarPublicidadService = new ConsultarPublicidadService();

        try {
            List<AnunciosPublicidadDTO> anunciosRegistrados = consultarPublicidadService.obtenerAnunciosPublicidad(numero);

            return Response.ok(anunciosRegistrados).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
