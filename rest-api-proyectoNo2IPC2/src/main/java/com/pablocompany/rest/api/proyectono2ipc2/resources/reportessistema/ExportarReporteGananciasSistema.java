/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportessistema;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.NombreReporteRandomService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.GananciasSistemaDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncianteDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ExportarGananciasAnuncianteService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ExportarGananciasSistemaService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ReporteGananciasAnuncianteService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ReporteGananciasSistemaService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pablo
 */
@Path("reportes/sistema/ganancias/sistema/exportar")
public class ExportarReporteGananciasSistema {

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

            ExportarGananciasSistemaService exportarReporteService = new ExportarGananciasSistemaService();

            byte[] pdfData = exportarReporteService.getReporteGanancias(reporteGananciasDto);

            StreamingOutput stream = output -> {

                output.write(pdfData);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreReporte = nombreReporteRandomService.getNombre("ReporteGanancias");

            return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + nombreReporte + "\"")
                    .header("Access-Control-Expose-Headers", "Content-Disposition")
                    .build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
