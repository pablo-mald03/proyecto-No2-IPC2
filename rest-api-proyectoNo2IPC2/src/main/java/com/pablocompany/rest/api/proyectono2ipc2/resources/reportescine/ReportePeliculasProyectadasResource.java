/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportescine;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.ReporteComentariosSalaService;
import jakarta.ws.rs.GET;
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
@Path("reportes/peliculas/proyectadas")
public class ReportePeliculasProyectadasResource {

    //Endpoint que permite obtener las salas proyectadas sin filtro
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/limit/{limite}/offset/{inicio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reportePeliculasSinFiltro() {

        return Response.ok().build();
    }

    //Enpoint que permite obtener la cantidad de peliculas proyectadas sin filtro
    @GET
    @Path("/cantidad/inicio/{fechaInicio}/fin/{fechaFin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cantidadComentarios(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin) {

        ReporteComentariosSalaService reporteComentariosSalaService = new ReporteComentariosSalaService();

        try {
            CantidadReportesDTO cantidadReportes = reporteComentariosSalaService.cantidadReportesSinFiltro(fechaInicio, fechaFin);
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
