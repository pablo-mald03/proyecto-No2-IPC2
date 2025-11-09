/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.administradorcine;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.CantidadRegistrosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.dtos.BilleteraCineRequest;
import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.models.BilleteraCineDTO;
import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.services.BilleteraCineCrudService;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.CineInformacionDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.services.CineCrudService;
import com.pablocompany.rest.api.proyectono2ipc2.cine.services.CinesInformacionService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.TransaccionInvalidaException;
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
@Path("cines/billetera")
public class BilleteraCineResource {

    //Endpoint que sirve para obtener la informacion de cines para el menu principal
    @GET
    @Path("asociada/limit/{limite}/offset/{inicio}/usuario/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCinesPrincipal(
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio,
            @PathParam("idUsuario") String idUsuario
    ) {

        BilleteraCineCrudService billeteraCine = new BilleteraCineCrudService();

        try {
            List<BilleteraCineDTO> listadoBilleteras = billeteraCine.obtenerCinesAsociados(limite, inicio, idUsuario);

            return Response.ok(listadoBilleteras).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite obtener la cantidad de billeteras digitales que posee el administrador de cine
    @GET
    @Path("/cantidad/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCantidadCines(
            @PathParam("idUsuario") String idUsuario
    ) {

        BilleteraCineCrudService billeteraCine = new BilleteraCineCrudService();

        try {
            CantidadRegistrosDTO cantidadRegistros = billeteraCine.obtenerCantidadBilleteras(idUsuario);
            return Response.ok(cantidadRegistros).build();

        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite obtener la referencia de la billetera digital 
    @GET
    @Path("/codigo/{codigoCine}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerBilleteraCodigo(
            @PathParam("codigoCine") String codigoCine
    ) {

        BilleteraCineCrudService billeteraCine = new BilleteraCineCrudService();

        try {
            CantidadRegistrosDTO cantidadRegistros = billeteraCine.obtenerCantidadBilleteras(codigoCine);
            return Response.ok(cantidadRegistros).build();

        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite recargar la billetera digital del cine
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response recargarBilleteraDigital(BilleteraCineRequest request) {

        BilleteraCineCrudService billeteraCine = new BilleteraCineCrudService();

        try {
            if (billeteraCine.recargarBilletera(request)) {
                return Response.ok().build();
            } else {
                throw new TransaccionInvalidaException("No se ha podido generar la recarga del saldo de la billetera digital de cine");
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
