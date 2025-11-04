/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.services;

import com.pablocompany.rest.api.proyectono2ipc2.billetera.models.BilleteraDigital;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.database.RolDB;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.database.UsuarioDB;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.DatosUsuario;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.TipoUsuarioEnum;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.Usuario;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase especializada en realizar todas las interacciones con la base de datos
public class UsuarioCrudService {

    //Metodo delegado para poder crear un nuevo usuario
    public boolean crearUsuario(Usuario usuarioNuevo, String confirmPassword) throws EntidadExistenteException, FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (usuarioNuevo.esUsuarioValido(confirmPassword, 0)) {

            RolDB rolDb = new RolDB();

            try {

                TipoUsuarioEnum tipoUsuario = TipoUsuarioEnum.valueOf(usuarioNuevo.getCodigoRol());

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

        throw new ErrorInesperadoException("No se ha podido registrar al usuario");
    }

    //Metodo utilizado para obtener la informacion del usuario
    public DatosUsuario obtenerDatosUsuario(String id) throws ErrorInesperadoException, FormatoInvalidoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(id)) {
            throw new FormatoInvalidoException("El identificador del usuario se envio vacio");
        }

        UsuarioDB usuarioDb = new UsuarioDB();

        return usuarioDb.obtenerInformacionUsuario(id);

    }

}
