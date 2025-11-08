/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author pablo
 */
//Clase utilizada para convertir imagenes 
public class ConvertirImagenAnuncioService {

    //Metodo encargado de parsear imagenes a png
    public byte[] convertirFormatoImagen(InputStream archivo) throws IOException, FormatoInvalidoException {

        BufferedImage imagen = null;

        if (archivo != null) {
            imagen = ImageIO.read(archivo);
        }

        if (imagen == null) {
            try (InputStream defaultImg = getClass().getResourceAsStream("/icons/defaultImgAnuncio.png")) {
                if (defaultImg == null) {
                    throw new IOException("No se encontró la imagen predeterminada");
                }
                imagen = ImageIO.read(defaultImg);
                if (imagen == null) {
                    throw new FormatoInvalidoException("La imagen predeterminada no es válida o está dañada");
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imagen, "png", baos);
        return baos.toByteArray();

    }

    //Metodo ecargado de validar imagenes
    public boolean esImagenValida(InputStream archivo) throws FormatoInvalidoException {

        try {
            BufferedImage imagen = ImageIO.read(archivo);
            return imagen != null;
        } catch (IOException e) {

            throw new FormatoInvalidoException("La imagen enviada esta vacia");
        }

    }

}
