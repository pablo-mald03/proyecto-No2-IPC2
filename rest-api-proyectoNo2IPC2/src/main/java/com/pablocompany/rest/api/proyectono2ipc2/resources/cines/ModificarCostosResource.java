/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.cines;

import com.pablocompany.rest.api.proyectono2ipc2.costocine.dtos.CostoDTORequest;
import com.pablocompany.rest.api.proyectono2ipc2.costocine.models.CostoModificacionDTO;
import com.pablocompany.rest.api.proyectono2ipc2.costocine.services.CostosCineCrudService;
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
@Path("cines/modificar/costos")
public class ModificarCostosResource {

    //Endpoint que permite crear/modificar los costos de los cines
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearCine(CostoDTORequest request) {

        CostosCineCrudService costosCineCrudService = new CostosCineCrudService();
        try {

            if (costosCineCrudService.modificarCosto(request)) {
                return Response.ok().build();
            } else {
                throw new ErrorInesperadoException("No se ha podido registrar el nuevo costo de cine");
            }

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite obtener el listado de costos relacionado a un cine
    @GET
    @Path("{codigoCine}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCostosCine(
            @PathParam("codigoCine") String codigoCine) {

        CostosCineCrudService costosCineCrudService = new CostosCineCrudService();

        try {
            List<CostoModificacionDTO> costosCine = costosCineCrudService.obtenerListadoCostos(codigoCine);

            return Response.ok(costosCine).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
