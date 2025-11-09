/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.peliculas;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.AnuncioRegistradoDTOResponse;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.services.AnunciosRegistradosService;
import com.pablocompany.rest.api.proyectono2ipc2.cine.services.CineCrudService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.peliculas.dtos.PeliculaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.peliculas.models.PeliculaDTO;
import com.pablocompany.rest.api.proyectono2ipc2.peliculas.service.PeliculaCurdService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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
@Path("peliculas")
public class PeliculaResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response crearCine(
            @FormDataParam("nombre") String nombre,
            @FormDataParam("sinopsis") String sinopsis,
            @FormDataParam("cast") String cast,
            @FormDataParam("director") String director,
            @FormDataParam("fechaEstreno") String fechaEstreno,
            @FormDataParam("clasificacion") String clasificacion,
            @FormDataParam("precio") String precio,
            @FormDataParam("duracion") Double duracion,
            @FormDataParam("poster") InputStream posterInputStream,
            @FormDataParam("poster") FormDataContentDisposition fileDetail) {

        PeliculaRequest peliculaRequest = new PeliculaRequest(nombre, sinopsis, cast, director, fechaEstreno, clasificacion, duracion, precio, posterInputStream, fileDetail);

        PeliculaCurdService peliculaCrudService = new PeliculaCurdService();
        try {

            if (peliculaCrudService.crearPelicula(peliculaRequest)) {
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

    @GET
    @Path("/todo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response peliculasRegistradas() {

        PeliculaCurdService peliculaCrudService = new PeliculaCurdService();

        try {
            List<PeliculaDTO> peliculasRegistradas = peliculaCrudService.obtenerPeliculas();

            return Response.ok(peliculasRegistradas).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
