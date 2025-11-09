/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.peliculas.service;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.services.ConvertirImagenAnuncioService;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.peliculas.database.PeliculaDB;
import com.pablocompany.rest.api.proyectono2ipc2.peliculas.dtos.PeliculaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.peliculas.models.Pelicula;
import com.pablocompany.rest.api.proyectono2ipc2.peliculas.models.PeliculaDTO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author pablo
 */
//Clase que se encarga de procesar las solicitudes para retornar data 
public class PeliculaCurdService {

    //Metodo que retorna la confirmacion de la accion
    public boolean crearPelicula(PeliculaRequest request) throws FormatoInvalidoException, DatosNoEncontradosException, ErrorInesperadoException {

        PeliculaDB peliculaDb = new PeliculaDB();

        Pelicula peliculaNueva = extraerDatos(request, peliculaDb);

        return peliculaDb.crearPelicula(peliculaNueva);
    }

    private Pelicula extraerDatos(PeliculaRequest request, PeliculaDB peliculaDb) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        ConvertirImagenAnuncioService convertirImagenService = new ConvertirImagenAnuncioService();

        try {

            byte[] poster = convertirImagenService.convertirFormatoImagen(request.getPoster());

            int cantidad = peliculaDb.cantidadPeliculas();

            LocalDate fechaEstreno = LocalDate.parse(request.getFechaEstreno());

            String codigo = generarCodigoPelicula(cantidad);

            return new Pelicula(
                    codigo,
                    request.getNombre(),
                    poster,
                    request.getSinopsis(),
                    request.getCast(),
                    fechaEstreno,
                    request.getDirector(),
                    Double.parseDouble(request.getPrecio()),
                    request.getClasificacion(),
                    request.getDuracion());

        } catch (IOException ex) {
            throw new ErrorInesperadoException("No se ha podido procesar la imagen");
        }

    }

    //Metodo que ayuda a auto nombrar el codigo de anuncio
    private String generarCodigoPelicula(int cantidadActual) {
        int siguiente = cantidadActual + 1;
        String numeroFormateado = String.format("%03d", siguiente);
        return "P-" + numeroFormateado;
    }

    
    //Retorna el listado de peliculas encontradas
    public List<PeliculaDTO> obtenerPeliculas() throws ErrorInesperadoException, FormatoInvalidoException {
        PeliculaDB peliculaDb = new PeliculaDB();

        return peliculaDb.peliculasRegistradas();
    }

}
