/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.database;

import com.pablocompany.rest.api.proyectono2ipc2.cine.models.CineAsociadoDTO;
import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.database.UsuarioDB;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author pablo
 */
//Clase delegada para gestionar la creacion de administradores de cine
public class UsuarioCineDB {

    //Constante que permite asignarle los cines especificados al administrador de cine
    private final String ASIGNAR_CINES = "INSERT INTO gestion_cine(id_usuario, codigo_cine) VALUES (?,?)";

    //Clase retornara la cantidad de registros hechos en los que se asigno el administrador
    public int asignarCines(List<CineAsociadoDTO> cinesAsociados, String idUsuario, Connection conexion) throws ErrorInesperadoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(ASIGNAR_CINES);) {

            conexion.setAutoCommit(false);

            for (CineAsociadoDTO cine : cinesAsociados) {

                preparedStmt.setString(1, idUsuario.trim());
                preparedStmt.setString(2, cine.getCodigo());
                preparedStmt.addBatch();

            }

            int[] resultados = preparedStmt.executeBatch(); 

            int totalInsertados = Arrays.stream(resultados).sum();

            return totalInsertados;

        } catch (SQLException ex) {
            throw new ErrorInesperadoException("No se han podido asignar los cines al administardor de cine");
        }

    }

    //Metodo utilizado para poder crear el administrador de cine
    public boolean crearAdministradorCine(Usuario usuarioNuevo, List<CineAsociadoDTO> cines, byte[] fotoPerfil, String codigoRol) throws ErrorInesperadoException, FormatoInvalidoException {

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try {

            conexion.setAutoCommit(false);

            UsuarioDB usuarioDb = new UsuarioDB();

            int filasAfectadas = usuarioDb.crearUsuario(usuarioNuevo, fotoPerfil, codigoRol, conexion);
            int filasAfectadasAsociaciones = asignarCines(cines,usuarioNuevo.getId(), conexion);

            if (filasAfectadas > 0 && filasAfectadasAsociaciones > 0) {

                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new ErrorInesperadoException("No se ha podido registrar al usuario administrador de cine. ");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback al contactar al administrador de cine al usuario administrador de cine");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden al crear usuario administrador de cine");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                throw new ErrorInesperadoException("Error al reactivar la autoconfirmacion al crear el usuario administrador de cine. Contactar Soporte tecnico.");
            }
        }

    }

}
