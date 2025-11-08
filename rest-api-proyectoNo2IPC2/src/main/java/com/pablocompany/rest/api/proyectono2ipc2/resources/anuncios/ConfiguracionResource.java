/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.anuncios;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.CambiarPrecioDTORequest;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.ConfiguracionAnuncioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.services.ConfiguracionAnunciosService;
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
import java.util.List;
import java.util.Map;

/**
 *
 * @author pablo
 */
@Path("configuraciones/anuncios")
public class ConfiguracionResource {

    //Endpoint que ayuda a retornar las configuraciones registrados en el sistema 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response anunciosRegistrados() {
        
        ConfiguracionAnunciosService configuracionAnunciosService = new ConfiguracionAnunciosService();
        
        try {
            List<ConfiguracionAnuncioDTO> configuraciones = configuracionAnunciosService.obtenerConfiguraciones();
            
            return Response.ok(configuraciones).build();
            
        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }
        
    }

    //Endpoint que permite obtener la configuracion por codigo
    @GET
    @Path("{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cantidadAnunciosComprados(
            @PathParam("codigo") String codigo) {
        
        ConfiguracionAnunciosService configuracionAnunciosService = new ConfiguracionAnunciosService();
        
        try {
            
            ConfiguracionAnuncioDTO configuracion = configuracionAnunciosService.obtenerConfiguracionCodigo(codigo);
            return Response.ok(configuracion).build();
            
        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }
    }

    //Endpoint que permite poder cambiar la configuracion de los anuncios
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cambiarPrecio(CambiarPrecioDTORequest request) {
        
        ConfiguracionAnunciosService configuracionAnunciosService = new ConfiguracionAnunciosService();
        
        try {
            
            if (configuracionAnunciosService.cambiarPrecioConfiguracion(request)) {
                return Response.ok().build();
            } else {
                throw new ErrorInesperadoException("No se ha podido cambiar el precio de la configuracion");
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
