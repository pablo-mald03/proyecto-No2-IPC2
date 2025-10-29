/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadNoExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.database.LoginDB;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.LoginRequest;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.UserLoggedDTO;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.LoginDTO;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase encargada de validar que las entradas sean validas y responder al login
public class LoginService {

    //Metodo encargado de autenticar al usuario
    public UserLoggedDTO autenticarUsuario(LoginRequest login) throws EntidadNoExistenteException, FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        LoginDTO usuarioLogin = extraerDatos(login);

        LoginDB loginDb = new LoginDB();
        
        if(loginDb.usuarioRegistrado(usuarioLogin)){
            
            return loginDb.obtenerRegistroUsuario(usuarioLogin);
            
        }
        
        throw new EntidadNoExistenteException("No existe ningun registro con las credenciales ingresadas");
        
    }

    //Metodo encargado de extraer los datos
    public LoginDTO extraerDatos(LoginRequest loginUser) throws FormatoInvalidoException {

        if (loginUser == null) {
            throw new FormatoInvalidoException("Request vacia");
        }

        if (StringUtils.isBlank(loginUser.getCorreo())) {
            throw new FormatoInvalidoException("Correo electronico vacio");
        }

        if (StringUtils.isBlank(loginUser.getPassword())) {
            throw new FormatoInvalidoException("Password vacia");
        }

        if (!correoValido(loginUser.getCorreo())) {
            throw new FormatoInvalidoException("Correo electronico invalido");
        }

        return new LoginDTO(
                loginUser.getCorreo(),
                loginUser.getPassword());

    }

    //Metodo que se encarga de comprobar que sea un correo valido
    private boolean correoValido(String correo) {

        return correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    }

}
