/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.database;

import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.Usuario;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author pablo
 */
//Clase delegada para operar con usuario en la base de datos
public class UsuarioDB {

    //Constante que verifica si los datos unico del usuario estan en uso
    private final String USUARIO_EXISTENTE = "SELECT identificacion FROM usuario WHERE correo = ? AND identificacion = ? AND id = ?";

    //Constante que permite verificar si el correo esta en uso
    private final String CORREO_EN_USO = "SELECT identificacion FROM usuario WHERE correo = ?";

    //Constante que permite verificar si la identificacion personal del usuario esta en uso
    private final String IDENTIFICACION_EN_USO = "SELECT identificacion FROM usuario WHERE identificacion = ?";

    //Constante que permite saber si el id del usuario ya esta en uso
    private final String ID_USUARIO_EN_USO = "SELECT identificacion FROM usuario WHERE id = ?";

    //Constante utilizada para crear el usuario en la pagina web
    private final String INSERTAR_USUARIO = "INSERT INTO usuario(id,correo,nombre,foto,password,telefono,pais,identificacion,codigo_rol) VALUES(?,?,?,?,?,?,?,?,?,?);";

    //Constante especial para el login
    private final String BUSCAR_USUARIO_ID = "SELECT us.id,us.nombre, us.identificacion, us.correo, us.telefono, us.pais, r.nombre AS `rol` FROM usuario us JOIN rol r  ON us.codigo_rol = r.codigo WHERE us.id = ?";

    private final String FOTO_USUARIO = "SELECT foto FROM usuario WHERE id = ?";

    //Metodo que permite comprobar si el usuario ya fue registrado en la base de datos
    public boolean exiteUsuario(Usuario usuarioNuevo) throws ErrorInesperadoException, EntidadExistenteException {

        if (usuarioNuevo == null) {
            throw new ErrorInesperadoException("Los datos del usuario estan vacios");
        }

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(USUARIO_EXISTENTE);) {
            query.setString(1, usuarioNuevo.getCorreo().trim());
            query.setString(2, usuarioNuevo.getIdentificacion().trim());
            query.setString(3, usuarioNuevo.getId().trim());
            ResultSet result = query.executeQuery();

            if (result.next()) {
                throw new EntidadExistenteException("Este usuario ya existe");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se permite Hacer inyecciones SQL en la verificaion de usuario");
        }

        try (PreparedStatement query1 = connection.prepareStatement(CORREO_EN_USO);) {
            query1.setString(1, usuarioNuevo.getCorreo().trim());
            ResultSet resultS = query1.executeQuery();

            if (resultS.next()) {
                throw new EntidadExistenteException("Este correo ya esta en uso");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se permite Hacer inyecciones SQL en la verificacion de correo");
        }

        try (PreparedStatement query2 = connection.prepareStatement(IDENTIFICACION_EN_USO);) {
            query2.setString(1, usuarioNuevo.getIdentificacion().trim());
            ResultSet resultS2 = query2.executeQuery();

            if (resultS2.next()) {
                throw new EntidadExistenteException("La identificacion de una persona es UNICA");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se permite Hacer inyecciones SQL en la verificacion de identificacion personal");
        }

        try (PreparedStatement query2 = connection.prepareStatement(ID_USUARIO_EN_USO);) {
            query2.setString(1, usuarioNuevo.getId().trim());
            ResultSet resultS2 = query2.executeQuery();

            if (resultS2.next()) {
                throw new EntidadExistenteException("Este identificador de usuario ya esta en uso");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se permite Hacer inyecciones SQL");
        }

        return false;

    }

    //Metodo que sirve para obtener la foto de perfil del usuario acorde al correo electronico
    public byte[] obtenerFotoPerfil(String id) throws ErrorInesperadoException {

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement preparedStmt = conexion.prepareStatement(FOTO_USUARIO);) {

            preparedStmt.setString(1, id.trim());

            try (ResultSet rs = preparedStmt.executeQuery()) {
                if (rs.next()) {

                    return rs.getBytes("foto");

                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {

            throw new ErrorInesperadoException("No se permiten inyecciones sql o patrones diferentes a los que se piden.");
        }

    }

    //Metodo que sirve para poder registrar un usuario en el sistema
    public boolean insertarUsuario(Usuario referenciUsuario) throws ErrorInesperadoException, IOException, FormatoInvalidoException {

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement preparedStmt = conexion.prepareStatement(INSERTAR_USUARIO);) {

            conexion.setAutoCommit(false);

            preparedStmt.setString(1, referenciUsuario.getId().trim());
            preparedStmt.setString(2, referenciUsuario.getCorreo().trim());
            preparedStmt.setString(3, referenciUsuario.getNombre().trim());
            preparedStmt.setBytes(4, referenciUsuario.getFotoPerfil());

            /*preparedStmt.setString(5, referenciUsuario.getpas().trim());
            preparedStmt.setString(5, referenciUsuario.getTelefono().trim());
            preparedStmt.setString(6, referenciUsuario.getOrganizacion().trim());

            preparedStmt.setString(8, referenciUsuario.getTipo().toString().trim());
            preparedStmt.setBigDecimal(9, new BigDecimal(referenciUsuario.getDinero()));
            preparedStmt.setBoolean(10, referenciUsuario.getEstado());*/

            int filasAfectadas = preparedStmt.executeUpdate();

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
                throw new ErrorInesperadoException("Error al hacer Rollback. Contactar a soporte tecnico al insertar el usuario");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden al insertar el usuario.");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                System.out.println("Error al reactivar la autoconfirmacion al insertar el usuario. Contactar Soporte tecnico.");
            }
        }

    }

}
