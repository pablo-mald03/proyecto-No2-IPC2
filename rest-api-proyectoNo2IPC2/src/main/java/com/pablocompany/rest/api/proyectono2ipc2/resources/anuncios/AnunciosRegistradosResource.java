/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.anuncios;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.AnuncioRegistradoDTOResponse;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.CambiarEstadoRequest;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.AnuncioDTORequest;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.services.AnunciosCrudService;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.services.AnunciosRegistradosService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.TransaccionInvalidaException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author pablo
 */
@Path("anuncios")
public class AnunciosRegistradosResource {

    //Endpoint utilizado para crear una compra del anuncios 
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response crearAnuncio(
            @FormDataParam("nombre") String nombre,
            @FormDataParam("codigoTipo") String codigoTipo,
            @FormDataParam("texto") String texto,
            @FormDataParam("url") String url,
            @FormDataParam("fechaCompra") String fechaCompra,
            @FormDataParam("tipoTarifa") String tipoTarifa,
            @FormDataParam("foto") InputStream foto,
            @FormDataParam("monto") String monto,
            @FormDataParam("foto") FormDataContentDisposition fotoDetalle,
            @FormDataParam("idUsuario") String idUsuario) {

        AnuncioDTORequest anuncioDTORequest = new AnuncioDTORequest(nombre, fechaCompra, url, texto, foto, fotoDetalle, codigoTipo, idUsuario, monto, tipoTarifa);

        AnunciosCrudService anunciosCrudService = new AnunciosCrudService();

        try {
            if (anunciosCrudService.comprarAnuncio(anuncioDTORequest)) {

                return Response.status(Response.Status.CREATED).build();
            } else {

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", "No se pudo comprar el anuncio")).build();

            }

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (TransaccionInvalidaException ex) {
            return Response.status(Response.Status.PAYMENT_REQUIRED).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Endpoint que ayuda a retornar los anuncios registrados en el sistema dinamicamente
    @GET
    @Path("/limit/{limite}/offset/{inicio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response anunciosRegistrados(
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        AnunciosRegistradosService anunciosRegistradosService = new AnunciosRegistradosService();

        try {
            List<AnuncioRegistradoDTOResponse> anunciosRegistrados = anunciosRegistradosService.obtenerAnunciosRegistrados(limite, inicio);

            return Response.ok(anunciosRegistrados).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Endpoint que permite obtener la cantidad total de anuncios comprados en el sistema
    @GET
    @Path("/cantidad")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cantidadAnunciosComprados() {

        AnunciosRegistradosService anunciosRegistradosService = new AnunciosRegistradosService();

        try {

            CantidadReportesDTO cantidadAnuncios = anunciosRegistradosService.cantidadReportesSinFiltro();
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

        AnunciosRegistradosService anunciosRegistradosService = new AnunciosRegistradosService();

        try {

            if (anunciosRegistradosService.cambiarEstadoAnuncio(request)) {
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
