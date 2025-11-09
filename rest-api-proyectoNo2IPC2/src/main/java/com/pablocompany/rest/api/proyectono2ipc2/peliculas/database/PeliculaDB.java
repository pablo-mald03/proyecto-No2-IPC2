/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.peliculas.database;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.AnuncioRegistradoDTOResponse;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.AnuncioRegistradoDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CantidadCargaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.peliculas.models.Pelicula;
import com.pablocompany.rest.api.proyectono2ipc2.peliculas.models.PeliculaDTO;
import com.pablocompany.rest.api.proyectono2ipc2.peliculas.service.ConvertirBase64ServicePeliculas;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ConvertirBase64Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo
 */
//Clase delegada para operar 
public class PeliculaDB {

    //constante utilizada para crear las peliculas
    private final String CREAR_ANUNCIO = "INSERT INTO pelicula (codigo, nombre, poster, sinopsis, casteo, fecha_estreno, director, precio, clasificacion, duracion) VALUES (?,?,?,?,?,?,?,?,?,?)";

    //Constante utilizada para crear las peliculas
    private final String CONSULTAR_PELICULAS = "SELECT codigo, nombre, poster, sinopsis, casteo, fecha_estreno, director, precio, clasificacion, duracion FROM pelicula";

    //Constante utilizada para obtener las peliculas por codigo
    private final String CONSULTAR_PELICULAS_POR_CODIGO = "SELECT codigo, nombre, poster, sinopsis, casteo, fecha_estreno, director, precio, clasificacion, duracion FROM pelicula WHERE codigo = ? LIMIT ? OFFSET ?";

    //Constante utilizada para crear las peliculas
    private final String CANTIDAD_PELICULAS = "SELECT COUNT(*) AS `cantidad` FROM pelicula";

    //Metodo delegado para OBTENER LA CANTIDAD DE PELICULAS REGISTRADAS
    public int cantidadPeliculas() throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_PELICULAS);) {

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de anuncios comprados en la web");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de anuncios comprados en la web");
        }
    }

    //Metodo que sirve para poder registrar un usuario en el sistema
    public boolean crearPelicula(Pelicula referenciaPelicula) throws ErrorInesperadoException, FormatoInvalidoException {

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try {

            conexion.setAutoCommit(false);

            int filasAfectadas = insertarPelicula(referenciaPelicula, conexion);

            if (filasAfectadas > 0) {

                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new ErrorInesperadoException("No se ha podido registrar al usuario. Contactar al administrador de Sistema. ");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback. Contactar a soporte tecnico al insertar la pelicula");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden al insertar la pelicula.");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                throw new ErrorInesperadoException("Error al reactivar la autoconfirmacion al insertar la pelicula. Contactar Soporte tecnico.");
            }
        }

    }

    private int insertarPelicula(Pelicula peliculaNueva, Connection conexion) throws ErrorInesperadoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(CREAR_ANUNCIO)) {

            preparedStmt.setString(1, peliculaNueva.getCodigo().trim());
            preparedStmt.setString(2, peliculaNueva.getNombre().trim());
            preparedStmt.setBytes(3, peliculaNueva.getPoster());
            preparedStmt.setString(4, peliculaNueva.getSinopsis().trim());
            preparedStmt.setString(5, peliculaNueva.getCast().trim());
            preparedStmt.setDate(6, java.sql.Date.valueOf(peliculaNueva.getFechaEstreno()));
            preparedStmt.setString(7, peliculaNueva.getPdirector().trim());
            preparedStmt.setDouble(8, peliculaNueva.getPrecio());
            preparedStmt.setString(9, peliculaNueva.getClasificacion().trim());
            preparedStmt.setDouble(10, peliculaNueva.getDuracion());

            int filasAfectadas = preparedStmt.executeUpdate();
            return filasAfectadas;

        } catch (SQLException ex) {
            throw new ErrorInesperadoException("No se ha podido generar la transacción para crear una nueva película");
        }
    }

    //Metodo que sirve para poder consultar el listado de peliculas
    public List<PeliculaDTO> peliculasRegistradas() throws ErrorInesperadoException, FormatoInvalidoException {

        ConvertirBase64ServicePeliculas convertirBase64Service = new ConvertirBase64ServicePeliculas();

        List<PeliculaDTO> listadoAnuncios = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_PELICULAS);) {

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                PeliculaDTO peliculaEncontrada = new PeliculaDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre"),
                        convertirBase64Service.convertirImagenAPngBase64(resultSet.getBytes("poster")),
                        resultSet.getString("sinopsis"),
                        resultSet.getString("casteo"),
                        resultSet.getDate("fecha_estreno").toLocalDate(),
                        resultSet.getString("director"),
                        resultSet.getBigDecimal("precio").doubleValue(),
                        resultSet.getString("clasificacion"),
                        resultSet.getBigDecimal("duracion").doubleValue()
                );

                listadoAnuncios.add(peliculaEncontrada);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de las peliculas");
        }

        return listadoAnuncios;
    }

}
