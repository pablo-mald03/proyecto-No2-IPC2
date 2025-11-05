/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.database;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.dtos.AdministradoresRequest;
import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.database.UsuarioDB;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.UsuarioDatosResponse;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.DatosUsuario;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.Usuario;
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
//Clase delegada para poder crear a los administradores de sistema
public class UsuarioSistemaDB {

    //Constante utilizada para poder retornar el listado de administradores registrados en la base de datos 
    private final String LISTADO_ADMINISTRADORES = "SELECT us.id, us.correo, us.nombre, us.foto, us.telefono, us.pais, us.identificacion, r.nombre AS `rol` FROM usuario us JOIN rol r  ON us.codigo_rol = r.codigo WHERE us.codigo_rol = ? LIMIT ? OFFSET ?";

    //Constante utilizada para saber la cantidad total de administradores de sistema
    private final String CANTIDAD_ADMINISTRADORES = "SELECT COUNT(*) FROM usuario us JOIN rol r  ON us.codigo_rol = r.codigo WHERE us.codigo_rol = ?";

    //Metodo delegado para obtener la cantidad de usuarios administradores de cine registrados
    public int cantidadRegistros(String codigoRol) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_ADMINISTRADORES);) {

            query.setString(1, codigoRol);

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de boletos comprados en salas de cine sin filtros");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de boletos vendidos Sin filtro" + e.getMessage());
        }
    }
    
    
    //Metodo delegado para poder crear administradores de sistema 
    public boolean crearAdministrador(Usuario referenciUsuario, byte[] fotoPerfil, String codigoRol) throws ErrorInesperadoException, FormatoInvalidoException{
        
        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try {

            conexion.setAutoCommit(false);

            UsuarioDB usuarioDb = new UsuarioDB();
            int filasAfectadas = usuarioDb.crearUsuario(referenciUsuario, fotoPerfil, codigoRol, conexion);

            if (filasAfectadas > 0 ) {
                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new ErrorInesperadoException("No se ha podido registrar el administrador de sistema. ");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback. Contactar a soporte tecnico al crear el administrador de sistema");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden al crear administrador de sistema.");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                System.out.println("Error al reactivar la autoconfirmacion al insertar el administrador. Contactar Soporte tecnico.");
            }
        }
        
    }

    //Metodo que retorna todo el listado de usuarios 
    public List<UsuarioDatosResponse> obtenerTodos(AdministradoresRequest request, String codigoRol) throws ErrorInesperadoException, FormatoInvalidoException {

        if (request == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<DatosUsuario> listadoUsuarios = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(LISTADO_ADMINISTRADORES);) {

            query.setString(1, codigoRol);
            query.setInt(2, request.getLimit());
            query.setInt(3, request.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                DatosUsuario usuarioEncontrado = new DatosUsuario(
                        resultSet.getString("id"),
                        resultSet.getString("correo"),
                        resultSet.getString("nombre"),
                        resultSet.getBytes("foto"),
                        resultSet.getString("telefono"),
                        resultSet.getString("pais"),
                        resultSet.getString("identificacion"),
                        resultSet.getString("rol")
                );

                listadoUsuarios.add(usuarioEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de comentarios en salas de cine");
        }

        List<UsuarioDatosResponse> listadoResponse = new ArrayList<>();
        for (DatosUsuario listadoUsuario : listadoUsuarios) {

            listadoResponse.add(new UsuarioDatosResponse(listadoUsuario));

        }

        return listadoResponse;
    }
}
