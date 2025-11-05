/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.administradorsistema;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.CantidadRegistrosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.services.UsuariosAdministradoresSistemaService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.UsuarioDatosResponse;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.Usuario;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
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
@Path("administrador/usuarios/sistema")
public class AdministradorSistemaResource {

    //Endpont que comunica al administrador de sistema para poder crear administradores de sistema
    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response crearAdministrador(
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

        UsuariosAdministradoresSistemaService usuariosAdministradoresSistemaService = new UsuariosAdministradoresSistemaService();

        try {

            if (usuariosAdministradoresSistemaService.crearAdministrador(usuarioNuevo, confirmPassword)) {
                return Response.status(Response.Status.CREATED).build();
            } else {

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", "No se pudo crear el adminisitrador de sistema")).build();
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

    //Endpoint que ayuda a obtener el listado general de administradores de sistema
    @GET
    @Path("/limit/{limite}/offset/{tope}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodosAdmins(
            @PathParam("limite") String limite,
            @PathParam("tope") String inicio) {

        UsuariosAdministradoresSistemaService usuariosAdministradoresService = new UsuariosAdministradoresSistemaService();

        try {
            List<UsuarioDatosResponse> usuariosResponse = usuariosAdministradoresService.obtenerAdministradores(limite, inicio);

            return Response.ok(usuariosResponse).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }
    }

    //Endpoint que permite obtener la cantidad de registros de administraodres de sistema
    @GET
    @Path("/cantidad")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCantidadRegistros() {

        UsuariosAdministradoresSistemaService usuariosAdministradoresService = new UsuariosAdministradoresSistemaService();

        try {
            CantidadRegistrosDTO cantidadReportes = usuariosAdministradoresService.cantidadRegistros();
            return Response.ok(cantidadReportes).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }
    }

}
