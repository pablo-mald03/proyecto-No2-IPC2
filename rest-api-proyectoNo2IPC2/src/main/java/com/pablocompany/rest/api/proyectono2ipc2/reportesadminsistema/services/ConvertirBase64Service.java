/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

/**
 *
 * @author pablo
 */
//Clase delegada para poder convertir a formato base64 una imagen de bytes
public class ConvertirBase64Service {

    //Metodo delegado para poder retornar una imagen en formato base64
    public String convertirImagenAPngBase64(byte[] imagenBytes) throws ErrorInesperadoException {
        try {
            BufferedImage imagenOriginal = ImageIO.read(new ByteArrayInputStream(imagenBytes));

            if (imagenOriginal == null) {
                throw new ErrorInesperadoException("El formato de imagen no es soportado");
            }

            ByteArrayOutputStream pngOutput = new ByteArrayOutputStream();
            ImageIO.write(imagenOriginal, "png", pngOutput);

            byte[] imagenPngBytes = pngOutput.toByteArray();

            return Base64.getEncoder().encodeToString(imagenPngBytes);

        } catch (IOException e) {
            throw new ErrorInesperadoException("No se pudo procesar la imagen extraida de la base de datos");
        }
    }

}
