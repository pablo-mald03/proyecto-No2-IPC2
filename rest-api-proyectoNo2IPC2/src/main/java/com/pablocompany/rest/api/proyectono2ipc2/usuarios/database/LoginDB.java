/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.database;

import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.UserLoggedDTO;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.LoginDTO;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.TipoUsuarioEnum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para tratar con la autenticacion de credenciales del usuario
public class LoginDB {

    //Constante que permite saber si el usuario cuenta con un registro en el sistema
    private final String LOGIN_USUARIO = "SELECT COUNT(*) AS `existencia` FROM usuario WHERE correo = ? AND password = ?";

    //Constante especial para el login
    private final String BUSCAR_USUARIO = "SELECT us.id, r.nombre AS `rol` FROM usuario AS `us` JOIN rol AS `r`  ON us.codigo_rol = r.codigo WHERE us.correo = ? AND us.password = ?";

    
    //Metodo que permite comprobasr si el usuario ya fue registrado en la base de datos
    public boolean usuarioRegistrado(LoginDTO usuarioLogin) throws ErrorInesperadoException, FormatoInvalidoException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(LOGIN_USUARIO);) {
            query.setString(1, usuarioLogin.getCorreo().trim());
            query.setString(2, usuarioLogin.getPasswordCifrada().trim());
            ResultSet result = query.executeQuery();

            if (result.next()) {
                return result.getInt("existencia") > 0;
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se permite Hacer inyecciones SQL en el login");
        }

        return false;

    }
    
    //Metodo que permite retornar las credenciales del usuario en base a los datos ingresados en el login
    public UserLoggedDTO obtenerRegistroUsuario(LoginDTO usuarioLogin) throws ErrorInesperadoException, FormatoInvalidoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(usuarioLogin.getCorreo())) {
            throw new FormatoInvalidoException("El correo del usuario esta vacio");
        }

        if (StringUtils.isBlank(usuarioLogin.getPassword())) {
            throw new FormatoInvalidoException("La contraseña del usuario esta vacia");
        }


        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(BUSCAR_USUARIO);) {

            query.setString(1, usuarioLogin.getCorreo().trim());
            query.setString(2, usuarioLogin.getPasswordCifrada().trim());

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {        
                return new UserLoggedDTO(
                        resultSet.getString("id"), 
                        TipoUsuarioEnum.valueOf( resultSet.getString("rol")));
            }
            else{
                throw  new DatosNoEncontradosException("No se ha encontrado el registro del usuario");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos del usuario login." );
        }

    }

    
}
