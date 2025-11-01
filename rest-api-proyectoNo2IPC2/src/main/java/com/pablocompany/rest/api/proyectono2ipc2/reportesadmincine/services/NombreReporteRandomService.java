/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author pablo
 */
//Clase delegada para poder generar un nombre de reporte optimo
public class NombreReporteRandomService {

    //Metodo que retorna un nombre optimo para poder exportar el reporte
    public String getNombre(String referencia) {

        LocalDateTime horaActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        
        String fechaHora = horaActual.format(formatter);

        String base = referencia.replaceAll("\\s+", "_");

        return base + "_" + fechaHora + ".pdf";

    }

}
