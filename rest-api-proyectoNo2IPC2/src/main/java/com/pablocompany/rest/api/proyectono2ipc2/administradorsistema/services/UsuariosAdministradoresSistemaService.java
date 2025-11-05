/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.services;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.database.UsuarioSistemaDB;
import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.dtos.AdministradoresRequest;
import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.CantidadRegistrosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.billetera.models.BilleteraDigital;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.database.RolDB;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.database.UsuarioDB;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.UsuarioDatosResponse;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.TipoUsuarioEnum;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.Usuario;
import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder gestionar las interacciones para obtener los datos o crear usuarios desde la api 
public class UsuariosAdministradoresSistemaService {

    //Metodo delegado para poder crear los administradores de sistema 
    public boolean crearAdministrador(Usuario usuarioNuevo, String confirmPassword) throws EntidadExistenteException, FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (usuarioNuevo.esUsuarioValido(confirmPassword, 0)) {

            RolDB rolDb = new RolDB();

            try {

                TipoUsuarioEnum tipoUsuario = TipoUsuarioEnum.ADMINISTRADOR_SISTEMA;

                String codigoRol = rolDb.obtenerCodigoRol(tipoUsuario);

                byte[] fotoPerfil = usuarioNuevo.getFotoPerfil();

                UsuarioDB usuarioDb = new UsuarioDB();

                if (!rolDb.rolesRegistrados()) {
                    rolDb.crearRoles();
                }

                if (!usuarioDb.exiteUsuario(usuarioNuevo)) {
                    BilleteraDigital billetera = new BilleteraDigital(0, usuarioNuevo.getId());
                    return usuarioDb.insertarUsuario(usuarioNuevo, fotoPerfil, codigoRol, billetera);
                }

            } catch (IllegalArgumentException e) {
                throw new FormatoInvalidoException("El rol del usuario no se ha especificado");
            } catch (IOException ex) {
                throw new ErrorInesperadoException("No se ha podido establecer una foto de perfil");
            }

        }

        throw new ErrorInesperadoException("No se ha podido registrar al administrador de sistema");
    }

    //Metodo delegado para obtener el listado de administradores de sistema
    public List<UsuarioDatosResponse> obtenerAdministradores(String limit, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        AdministradoresRequest reporteRequest = extraerDatos(limit, offset);

        UsuarioSistemaDB usuarioSistemaDb = new UsuarioSistemaDB();

        return usuarioSistemaDb.obtenerTodos(reporteRequest);
    }

    //Metodo delegado para poder validar y extraer la solicitud de request
    private AdministradoresRequest extraerDatos(String limit, String offset) throws FormatoInvalidoException {

        if (StringUtils.isBlank(limit)) {
            throw new FormatoInvalidoException("El limite superior de la peticion vacio");
        }

        if (StringUtils.isBlank(offset)) {
            throw new FormatoInvalidoException("El limite inferior de la peticion vacio");
        }

        if (!StringUtils.isNumeric(limit)) {
            throw new FormatoInvalidoException("El limite superior de la peticion no es numerico");
        }

        if (!StringUtils.isNumeric(offset)) {
            throw new FormatoInvalidoException("El limite inferior de la peticion no es numerico");
        }

        try {
            int inicioLimit = Integer.parseInt(limit);
            int inicioOffset = Integer.parseInt(offset);

            return new AdministradoresRequest(
                    inicioOffset,
                    inicioLimit);

        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son enteros");
        }

    }

    //Metodo que sirve para retornar la cantidad de usuarios administradores de sistema registrados en el sistema
    public CantidadRegistrosDTO cantidadRegistros() throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        try {

            UsuarioSistemaDB usuarioSistemaDb = new UsuarioSistemaDB();

            AdministradoresRequest intervaloRequest = new AdministradoresRequest(0, 0);

            int cantidad = usuarioSistemaDb.cantidadRegistros(intervaloRequest);

            return new CantidadRegistrosDTO(cantidad);

        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }

    }
}
