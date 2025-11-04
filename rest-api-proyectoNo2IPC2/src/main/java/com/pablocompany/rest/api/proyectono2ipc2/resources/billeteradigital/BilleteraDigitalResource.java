/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.billeteradigital;

import com.pablocompany.rest.api.proyectono2ipc2.billetera.dtos.BilleteraDigitalRequest;
import com.pablocompany.rest.api.proyectono2ipc2.billetera.dtos.SaldoBilleteraResponse;
import com.pablocompany.rest.api.proyectono2ipc2.billetera.models.SaldoBilleteraDTO;
import com.pablocompany.rest.api.proyectono2ipc2.billetera.services.BilleteraDigitalCrudService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
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
@Path("billetera")
public class BilleteraDigitalResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerSaldo(@PathParam("id") String idUsuario) {

        BilleteraDigitalCrudService billeteraDigitalCrudService = new BilleteraDigitalCrudService();
        try {
            SaldoBilleteraDTO billeteraDto = billeteraDigitalCrudService.obtenerSaldo(idUsuario);
            return Response.ok(new SaldoBilleteraResponse(billeteraDto)).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response obtenerSaldo(BilleteraDigitalRequest request) {

        BilleteraDigitalCrudService billeteraDigitalCrudService = new BilleteraDigitalCrudService();
        try {

            if (billeteraDigitalCrudService.recargarBilletera(request)) {

                return Response.ok(Response.ok()).build();
            } else {
                throw new ErrorInesperadoException("No se ha podido generar la transaccion");
            }

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
