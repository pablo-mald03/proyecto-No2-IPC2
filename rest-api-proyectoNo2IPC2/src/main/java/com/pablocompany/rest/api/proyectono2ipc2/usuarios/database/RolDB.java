/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.database;

import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.TipoUsuarioEnum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author pablo
 */
//Clase delegada para gestionar los roles 
public class RolDB {

    //Constante que sirve para comprobar que los roles esten instanciados correctamente
    private final String CONTEO_ROLES = "SELECT COUNT(*) AS `existencia` FROM rol";

    //Constante que sirve para traer el codigo del rol en base a su nombre
    private final String CODIGO_ROL = "SELECT codigo FROM rol WHERE nombre = ?";

    private final String ELIMINAR_ROLES = "DELETE FROM rol";

    private final String CREAR_ROLES = """
        INSERT INTO rol (codigo, nombre) VALUES
        (1, 'USUARIO'),
        (2, 'USUARIO_ESPECIAL'),
        (3, 'ADMINISTRADOR_SISTEMA'),
        (4, 'ADMINISTRADOR_CINE')
        """;

    //Metodo utilizado para obtener el codigo del rol
    public String obtenerCodigoRol(TipoUsuarioEnum tipoUsuario) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CODIGO_ROL);) {

            query.setString(1, tipoUsuario.name());
            
            ResultSet result = query.executeQuery();

            if (result.next()) {
                return result.getString("codigo");
            }else{
                throw new DatosNoEncontradosException("No se ha encontrado el codigo del Rol" + tipoUsuario.toString());
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se permiten hacer inyecciones SQL en la tabla de Rol");
        }

       
    }

    //Metodo que avisa si todo esta en orden en los roles
    public boolean rolesRegistrados() throws ErrorInesperadoException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONTEO_ROLES);) {

            ResultSet result = query.executeQuery();

            if (result.next()) {
                return result.getInt("existencia") == 4;
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se permite Hacer inyecciones SQL");
        }

        return false;

    }

    //Metodo que sirve para recrear los roles si falta alguno
    public void crearRoles() throws ErrorInesperadoException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try {
            connection.setAutoCommit(false); // 👈 Desactivamos autocommit (para poder hacer rollback)

            try (PreparedStatement eliminar = connection.prepareStatement(ELIMINAR_ROLES)) {
                eliminar.executeUpdate();
            }

            try (PreparedStatement insertar = connection.prepareStatement(CREAR_ROLES)) {
                insertar.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {

                throw new ErrorInesperadoException("Error al hacer rollback en la recreación de roles");
            }
            throw new ErrorInesperadoException("Error al recrear los roles en la base de datos");
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new ErrorInesperadoException("Error al activar el auto commit de la base de datos");
            }
        }

    }

}
