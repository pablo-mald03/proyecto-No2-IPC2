/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.GananciasSistemaDTO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author pablo
 */
//Clase delegada que permite exportar los reportes de ganancias en general del sistema
public class ExportarGananciasSistemaService {

    //Metodo utilizado para retornar el reporte de las ganancias del sistema
    public byte[] getReporteGanancias(GananciasSistemaDTO reporteGanancias) throws ErrorInesperadoException {

        InputStream logoEmpresa = getClass().getClassLoader().getResourceAsStream("com/pablocompany/rest/api/reports/img/cinemaAppIcon.png");

        if (logoEmpresa == null) {
            throw new ErrorInesperadoException("No se ha podido obtener el logo de la empresa");
        }

        InputStream reporte = getClass().getClassLoader().getResourceAsStream("com/pablocompany/rest/api/reports/reporteganancias/ReporteGanancias.jasper");

        if (reporte == null) {
            throw new ErrorInesperadoException("No se ha podido cargar el reporte Jasper");
        }

        List<GananciasSistemaDTO> listadoReporte = new ArrayList<>();

        listadoReporte.add(reporteGanancias);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listadoReporte);

        Map<String, Object> params = new HashMap<>();

        params.put("logoEmpresa", logoEmpresa);
        try {
            JasperPrint jasperPrinter = JasperFillManager.fillReport(reporte, params, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrinter);

        } catch (JRException ex) {
            throw new ErrorInesperadoException("No se ha podido generar el reporte de ganancias del sistema");
        }

    }
}
