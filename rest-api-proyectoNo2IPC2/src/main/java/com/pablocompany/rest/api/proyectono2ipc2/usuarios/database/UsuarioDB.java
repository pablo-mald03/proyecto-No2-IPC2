/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.database;

import com.pablocompany.rest.api.proyectono2ipc2.billetera.database.BilleteraDigitalDB;
import com.pablocompany.rest.api.proyectono2ipc2.billetera.models.BilleteraDigital;
import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadNoExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.CambioCredencialesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.DatosUsuario;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.Usuario;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para operar con usuario en la base de datos
public class UsuarioDB {

    //Constante que verifica si los datos unico del usuario estan en uso
    private final String USUARIO_EXISTENTE = "SELECT identificacion FROM usuario WHERE correo = ? AND identificacion = ? AND id = ?";

    //Constante que permite verificar si el usuario existe en base a su id y su correo
    private final String CREDENCIALES_EXISTENTES = "SELECT identificacion FROM usuario WHERE correo = ? AND id = ?";

    //Constante que permite verificar si el correo esta en uso
    private final String CORREO_EN_USO = "SELECT identificacion FROM usuario WHERE correo = ?";

    //Constante que permite verificar si la identificacion personal del usuario esta en uso
    private final String IDENTIFICACION_EN_USO = "SELECT identificacion FROM usuario WHERE identificacion = ?";

    //Constante que permite saber si el id del usuario ya esta en uso
    private final String ID_USUARIO_EN_USO = "SELECT identificacion FROM usuario WHERE id = ?";

    //Constante utilizada para crear el usuario en la pagina web
    private final String INSERTAR_USUARIO = "INSERT INTO usuario(id,correo,nombre,foto,password,telefono,pais,identificacion,codigo_rol) VALUES(?,?,?,?,?,?,?,?,?);";

    //Constante especial para el login
    private final String BUSCAR_USUARIO_ID = "SELECT us.id, us.correo, us.nombre, us.foto, us.telefono, us.pais, us.identificacion, r.nombre AS `rol` FROM usuario us JOIN rol r  ON us.codigo_rol = r.codigo WHERE us.id = ?";

    //Constante que permite obtener la foto de perfil del usuario
    private final String FOTO_USUARIO = "SELECT foto FROM usuario WHERE id = ?";

    //Constante que permite cambiar las credenciales del usuario
    private final String REESTABLECER_CREDENCIALES = "UPDATE usuario SET password = ? WHERE correo = ? AND id = ?";

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

    //Metodo que comprueba si el usuario cuenta con un registro en el sistema para el cambio de credenciales 
    public boolean credencialesExistentes(String correo, String idUsuario) throws EntidadNoExistenteException, FormatoInvalidoException, ErrorInesperadoException {

        if (StringUtils.isBlank(correo)) {
            throw new FormatoInvalidoException("El correo del usuario esta vacio");
        }

        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El id del usuario esta vacio");
        }

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query1 = connection.prepareStatement(CREDENCIALES_EXISTENTES);) {
            query1.setString(1, correo.trim());
            query1.setString(2, idUsuario.trim());
            ResultSet resultS = query1.executeQuery();

            if (resultS.next()) {
                return true;
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se permite Hacer inyecciones SQL en la verificacion de credenciales");
        }

        return false;
    }

    //Metodo delegado para poder reestablecer/cambiar la password del usuario
    public boolean reestablecerCredenciales(CambioCredencialesDTO nuevasCredenciales) throws ErrorInesperadoException, FormatoInvalidoException {

        if (nuevasCredenciales == null) {
            throw new FormatoInvalidoException("No se ha enviado ninguna informacion sobre las credenciales");
        }

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement preparedStmt = conexion.prepareStatement(REESTABLECER_CREDENCIALES);) {

            conexion.setAutoCommit(false);
            preparedStmt.setString(1, nuevasCredenciales.getPasswordNueva().trim());
            preparedStmt.setString(2, nuevasCredenciales.getCorreo().trim());
            preparedStmt.setString(3, nuevasCredenciales.getIdUsuario().trim());

            int filasAfectadas = preparedStmt.executeUpdate();

            if (filasAfectadas > 0) {

                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new ErrorInesperadoException("No se ha podido reestablecer las credenciales del usuario. Contactar al administrador de Sistema. ");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback al reestablecer las credenciales del usuario");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden al cambiar credenciales.");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                System.out.println("Error al reactivar la autoconfirmacion al editar las credenciales del usuario.");
            }
        }
    }

    //Metodo que sirve para poder registrar un usuario en el sistema
    public boolean insertarUsuario(Usuario referenciUsuario, byte[] fotoPerfil, String codigoRol, BilleteraDigital billetera) throws ErrorInesperadoException, IOException, FormatoInvalidoException {

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try {

            conexion.setAutoCommit(false);

            BilleteraDigitalDB billeteraDigitalDb = new BilleteraDigitalDB();

            int filasAfectadas = crearUsuario(referenciUsuario, fotoPerfil, codigoRol, conexion);
            int filasAfectadasBilletera = billeteraDigitalDb.insertarBilletera(billetera, conexion);

            if (filasAfectadas > 0 && filasAfectadasBilletera > 0) {

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

    //Metodo que sirve para poder generar la transaccion para insertar al usuario y crearle su registro a su bileltera digital
    public int crearUsuario(Usuario referenciUsuario, byte[] fotoPerfil, String codigoRol, Connection conexion) throws SQLException, FormatoInvalidoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(INSERTAR_USUARIO);) {

            conexion.setAutoCommit(false);

            preparedStmt.setString(1, referenciUsuario.getId().trim());
            preparedStmt.setString(2, referenciUsuario.getCorreo().trim());
            preparedStmt.setString(3, referenciUsuario.getNombre().trim());
            preparedStmt.setBytes(4, fotoPerfil);

            preparedStmt.setString(5, referenciUsuario.getPasswordCifrada().trim());
            preparedStmt.setString(6, referenciUsuario.getTelefono().trim());
            preparedStmt.setString(7, referenciUsuario.getPais().trim());

            preparedStmt.setString(8, referenciUsuario.getIdentificacion().trim());

            preparedStmt.setString(9, codigoRol);

            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        }

    }

    //Metodo que permite retornar las credenciales del usuario en base a los datos ingresados en el login
    public DatosUsuario obtenerInformacionUsuario(String codigo) throws ErrorInesperadoException, FormatoInvalidoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(codigo)) {
            throw new FormatoInvalidoException("El codigo del usuario esta vacio");
        }

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(BUSCAR_USUARIO_ID);) {

            query.setString(1, codigo.trim());

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                return new DatosUsuario(
                        resultSet.getString("id"),
                        resultSet.getString("correo"),
                        resultSet.getString("nombre"),
                        resultSet.getBytes("foto"),
                        resultSet.getString("telefono"),
                        resultSet.getString("pais"),
                        resultSet.getString("identificacion"),
                        resultSet.getString("rol")
                );
            } else {
                throw new DatosNoEncontradosException("No se ha encontrado el registro del usuario");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos del usuario login.");
        }

    }

}
