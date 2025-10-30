/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadNoExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.UsuarioDatosResponse;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.DatosUsuario;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.Usuario;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.services.UsuarioCrudService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Map;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author pablo
 */
@Path("usuarios")
public class UsuarioResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response crearUsuario(
            @FormDataParam("id") String id,
            @FormDataParam("correo") String correo,
            @FormDataParam("nombre") String nombre,
            @FormDataParam("password") String password,
            @FormDataParam("confirmPassword") String confirmPassword,
            @FormDataParam("telefono") String telefono,
            @FormDataParam("pais") String pais,
            @FormDataParam("identificacion") String identificacion,
            @FormDataParam("codigoRol") String codigoRol,
            @FormDataParam("foto") InputStream foto,
            @FormDataParam("foto") FormDataContentDisposition fotoDetalle
    ) {

        Usuario usuarioNuevo = new Usuario(id, correo, nombre, password, telefono, pais, identificacion, codigoRol, foto, fotoDetalle);

        UsuarioCrudService crudService = new UsuarioCrudService();

        try {
            if (crudService.crearUsuario(usuarioNuevo, confirmPassword)) {

                return Response.status(Response.Status.CREATED).build();
            } else {

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", "No se pudo crear el usuario")).build();

            }
        } catch (EntidadExistenteException ex) {

            return Response.status(Response.Status.CONFLICT).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response datosUsuario(@PathParam("id") String id) {

        UsuarioCrudService crudService = new UsuarioCrudService();

        try {
            DatosUsuario datosUsuario = crudService.obtenerDatosUsuario(id);
            return Response.ok(new UsuarioDatosResponse(datosUsuario)).build();

        } catch (FormatoInvalidoException ex) {
            //Indica que la solicitud no fue procesada
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            //Indica algun error de procesamiento de informacion
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
           return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
