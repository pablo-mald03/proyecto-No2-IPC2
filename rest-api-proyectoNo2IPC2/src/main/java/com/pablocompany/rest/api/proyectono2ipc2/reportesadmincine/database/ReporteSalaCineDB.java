/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.database;

import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author pablo
 */
//Clase delegada para generar las querys de los reportes
public class ReporteSalaCineDB {
    
    //Constante que permite obtener el reporte de comentarios sin filtro de sala
    private final String REPORTE_COMENTARIOS = "SELECT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala WHERE co.fecha_posteo BETWEEN ? AND ? ORDER BY co.fecha_posteo DESC LIMIT ? OFFSET ?";
    
    //Constante que permite obtener el conteo de cantidad de reportes que hay en base a una fecha
    private final String CANTIDAD_REPORTES = "SELECT COUNT(*) AS `cantidad` FROM sala AS `sa` JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala WHERE co.fecha_posteo BETWEEN ? AND ? ";
   
    //Constante que permite obtener el conteo de cantidad de reportes que hay en base a una fecha con filtro por sala de cine
    private final String CANTIDAD_REPORTES_FILTRO = "SELECT COUNT(*) AS `cantidad` FROM sala AS `sa` JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala WHERE sa.codigo = ? AND co.fecha_posteo BETWEEN ? AND ? ";

    //Constante que permite obtener el preorte de comentarios en base al filtro de sala
    private final String COMENTARIOS_SALAS = "SELECT co.id_usuario, co.contenido, co.fecha_posteo FROM sala AS `sa` JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala WHERE sa.codigo = ? AND co.fecha_posteo BETWEEN ? AND ? ORDER BY co.fecha_posteo DESC LIMIT ? OFFSET ?";
    
    //Constante que permite obtener los comentarios de una sala de cine con filtro de sala de cine
    private final String REPORTE_COMENTARIOS_FILTRO = "SELECT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala WHERE sa.codigo = ? AND co.fecha_posteo BETWEEN ? AND ? ORDER BY co.fecha_posteo DESC LIMIT ? OFFSET ?";
    
    //Metodo delegado para obtener la cantidad de reportes que se tienen en el intervalo de fechas
    public int cantidadReportes() throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_REPORTES);) {

            

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de comentarios de salas de cine");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de salas de cine");
        }
    }
    
    //Metodo delegado para obtener la cantidad de reportes que se tienen en el intervalo de fechas con filtro
    public int cantidadReportesFiltro() throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_REPORTES_FILTRO);) {

            

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de comentarios de salas de cine");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de salas de cine");
        }
    }
    
    
    

}
