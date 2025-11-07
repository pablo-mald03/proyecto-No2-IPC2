/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.cines;

import com.pablocompany.rest.api.proyectono2ipc2.billetera.dtos.BilleteraDigitalRequest;
import com.pablocompany.rest.api.proyectono2ipc2.billetera.services.BilleteraDigitalCrudService;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CineRequest;
import com.pablocompany.rest.api.proyectono2ipc2.cine.services.CineCrudService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

/**
 *
 * @author pablo
 */
@Path("cines")
public class CineResource {
    
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
    
    
}
