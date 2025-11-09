/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportessistema;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.GananciasSistemaDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ReporteGananciasSistemaService;
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
@Path("reportes/sistema/ganancias/anunciantes")
public class ReporteGananciasAnuncianteResource {

    //Endpoint que permite obtener las ganancias del sistema
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reporteAnunciantesSinFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin) {

        ReporteGananciasSistemaService reporteGananciasSistemaService = new ReporteGananciasSistemaService();

        try {
            GananciasSistemaDTO reporteGananciasDto = reporteGananciasSistemaService.obtenerReporteGanancias(fechaInicio, fechaFin);

            return Response.ok(reporteGananciasDto).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
