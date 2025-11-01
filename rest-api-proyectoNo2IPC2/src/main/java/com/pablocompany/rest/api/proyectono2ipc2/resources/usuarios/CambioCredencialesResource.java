/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.usuarios;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadNoExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.CambioCredencialesRequest;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.services.CambioCredencialesService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

/**
 *
 * @author pablo
 */
@Path("reestablecer/credenciales")
public class CambioCredencialesResource {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reestablecerPassword(CambioCredencialesRequest cambioCredenciales) {

        CambioCredencialesService cambioCredencialesService = new CambioCredencialesService();

        try {

            if (cambioCredencialesService.cambiarCredenciales(cambioCredenciales)) {
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                throw new ErrorInesperadoException("No se han podido reestablecer las credenciales");
            }

        } catch (EntidadNoExistenteException ex) {
            //Indica que no se encontro la entidad
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();

        } catch (FormatoInvalidoException ex) {
            //Indica que la solicitud no fue procesada
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            //Indica algun error de procesamiento de informacion
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
