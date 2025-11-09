/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.cines;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.CantidadRegistrosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.CineAsociadoDTOResponse;
import com.pablocompany.rest.api.proyectono2ipc2.cine.services.CineCrudService;
import com.pablocompany.rest.api.proyectono2ipc2.cine.services.CinesInformacionService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
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
@Path("cines/informacion")
public class CineInformacionResource {

    //Endpoint que sirve para obtener la informacion de cines modo llave valor
    @GET
    @Path("llave/limit/{limite}/offset/{inicio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodosCines(
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        CinesInformacionService cinesInformacionService = new CinesInformacionService();

        try {
            List<CineAsociadoDTOResponse> listadoCines = cinesInformacionService.obtenerCinesAsociadosValores(limite, inicio);

            return Response.ok(listadoCines).build();

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
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
