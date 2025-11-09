/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.peliculas.service;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

/**
 *
 * @author pablo
 */

//Clase delegada para convertir las peliculas a base 64
public class ConvertirBase64ServicePeliculas {

    //Metodo delegado para poder retornar una imagen en formato base64
    public String convertirImagenAPngBase64(byte[] imagenBytes) throws ErrorInesperadoException {

        try {

            BufferedImage imagenOriginal = ImageIO.read(new ByteArrayInputStream(imagenBytes));

            if (imagenOriginal == null) {
                return obtenerImagenDefaultBase64();
            }

            ByteArrayOutputStream pngOutput = new ByteArrayOutputStream();
            ImageIO.write(imagenOriginal, "png", pngOutput);

            return Base64.getEncoder().encodeToString(pngOutput.toByteArray());

        } catch (IOException e) {
            return obtenerImagenDefaultBase64();
        }
    }

    //Submetodo que retorna una imagen default para evitar errores
    private String obtenerImagenDefaultBase64() throws ErrorInesperadoException {
        try (InputStream reporte = getClass().getClassLoader()
                .getResourceAsStream("com/pablocompany/rest/api/reports/imagesdefault/defaultImg.png")) {

            if (reporte == null) {
                throw new ErrorInesperadoException("No se pudo cargar la imagen default");
            }

            byte[] bytesDefault = reporte.readAllBytes();
            return Base64.getEncoder().encodeToString(bytesDefault);

        } catch (IOException e) {
            throw new ErrorInesperadoException("No se pudo procesar la imagen default");
        }
    }
}
