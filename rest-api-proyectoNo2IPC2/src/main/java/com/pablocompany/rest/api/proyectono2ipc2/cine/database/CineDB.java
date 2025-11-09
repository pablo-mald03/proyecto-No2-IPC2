/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine.database;

import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.database.BilleteraCineDB;
import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.models.BilleteraCine;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CantidadCargaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.Cine;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.CineAsociadoDTOResponse;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.CineDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.CineInformacionDTO;
import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.costocine.database.CostoCineDB;
import com.pablocompany.rest.api.proyectono2ipc2.costocine.models.CostoCine;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase Delgada para poder interactuar con la base de datos para poder gestionar los cines
public class CineDB {

    //Constante que sirve para poder crear a los cines
    private final String CREAR_CINE = "INSERT INTO cine (codigo, nombre, estado_anuncios, monto_ocultacion, fecha_creacion, descripcion, ubicacion) VALUES(?,?,?,?,?,?,?)";

    //Constante que permite obtener la cantidad de cines registrados en el sistema
    private final String CANTIDAD_CINES = "SELECT COUNT(*) AS `cantidad` FROM cine";

    //Constante que permite obtener los cines que estan creados en el sistema 
    private final String CONSULTAR_CINES = "SELECT codigo, nombre, estado_anuncios, monto_ocultacion, fecha_creacion, descripcion, ubicacion FROM cine LIMIT ? OFFSET ?";

    //-------------APARTADO DE EDICION----------------------
    //Constante que permite obtener los cines que estan creados en el sistema 
    private final String CONSULTAR_CINES_CODIGO = "SELECT codigo, nombre, estado_anuncios, monto_ocultacion, fecha_creacion, descripcion, ubicacion FROM cine WHERE codigo = ?";

    //Constante que permite obtener los nombres de los cines para poderlos ediar
    private final String EDITAR_CINE = "UPDATE cine SET nombre = ?, monto_ocultacion = ?, descripcion = ?, ubicacion = ? WHERE codigo = ?";

    //Constante que permite obtener el nombre de un cine en base a codigo
    private final String CONSULTAR_NOMBRE_CINE = "SELECT nombre FROM cine WHERE nombre = ? LIMIT 1";

    //Constante que permite obtener el nombre en base al codigo y el nombre
    private final String CONSULTAR_NOMBRE_CINE_END = "SELECT nombre FROM cine WHERE nombre = ? AND codigo = ? LIMIT 1";

    //-------------FIN DEL APARTADO DE EDICION----------------------
    //Constante que permite obtener los cines registrados en la aplicacion para el dashboard del usuario 
    private final String CONSULTAR_CINE_DASHBOARD = "SELECT codigo, nombre, descripcion, ubicacion FROM cine ORDER BY fecha_creacion DESC LIMIT ? OFFSET ?";

    //Constante que permite obtener el cine seleccionado para el dashboard del usuario 
    private final String CONSULTAR_CINE_CODIGO_DASHBOARD = "SELECT codigo, nombre, descripcion, ubicacion FROM cine WHERE codigo = ?";

    //Constante que permite obtener el cine seleccionado para el dashboard del usuario 
    private final String CONSULTAR_CINE_CODIGO_VALOR = "SELECT codigo, nombre FROM cine LIMIT ? OFFSET ?";

    //=============================APARTADO DONDE SE MUESTRAN LOS DATOS DE LOS CINES A LOS ADMINISTRADORES DE CINES==========================
    //Constante que permite retornar los cines en los que se encuentra asignado el adminstrador de cine
    private final String CINES_ASOCIADOS_CODIGO = "SELECT c.codigo, c.nombre, c.estado_anuncios, c.monto_ocultacion, c.fecha_creacion, c.descripcion, c.ubicacion FROM cine c JOIN gestion_cine gc ON gc.codigo_cine = c.codigo WHERE gc.id_usuario = ? LIMIT ? OFFSET ?";

       //Constante que permite obtener la cantidad de cines a los que esta asociado un administrador de cine
    private final String CANTIDAD_CINES_ASOCIADOS = "SELECT COUNT(*) AS `cantidad` FROM cine c JOIN gestion_cine gc ON gc.codigo_cine = c.codigo WHERE gc.id_usuario = ?";

    
    
    
    //Metodo que sirve para poder registrar un nuevo cine en el sistema
    public boolean crearNuevoCine(CineDTO cineNuevo) throws ErrorInesperadoException, FormatoInvalidoException {

        if (cineNuevo == null) {
            throw new FormatoInvalidoException("No se ha enviado ninguna informacion sobre el cine a crear");
        }

        BilleteraCineDB billeteraCineDb = new BilleteraCineDB();
        CostoCineDB costoCineDb = new CostoCineDB();

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try {

            conexion.setAutoCommit(false);

            int filasAfectadas = insertarNuevoCine(conexion, cineNuevo);

            int filasAfectadasCosto = costoCineDb.registrarCostoCine(new CostoCine(cineNuevo.getCostoCine(), cineNuevo.getFechaCreacion(), cineNuevo.getCodigo()), conexion);

            int filasAfectadasBilletera = billeteraCineDb.crearBilleteraCine(new BilleteraCine(0, cineNuevo.getCodigo().trim()), conexion);

            if (filasAfectadas > 0 && filasAfectadasBilletera > 0 && filasAfectadasCosto > 0) {

                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new ErrorInesperadoException("No se ha podido crear el cine.");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback al crear un nuevo cine");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden al crear un nuevo cine");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                System.out.println("Error al reactivar la autoconfirmacion al crear un nuevo cine");
            }
        }
    }

    //Metodo que sirve para poder generar la transaccion para insertar cine
    public int insertarNuevoCine(Connection conexion, CineDTO cineNuevo) throws ErrorInesperadoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(CREAR_CINE);) {

            preparedStmt.setString(1, cineNuevo.getCodigo());
            preparedStmt.setString(2, cineNuevo.getNombre());
            preparedStmt.setBoolean(3, cineNuevo.isEstadoAnuncio());
            preparedStmt.setBigDecimal(4, new BigDecimal(cineNuevo.getMontoOcultacion()));
            preparedStmt.setDate(5, java.sql.Date.valueOf(cineNuevo.getFechaCreacion()));
            preparedStmt.setString(6, cineNuevo.getDescripcion());
            preparedStmt.setString(7, cineNuevo.getUbicacion());

            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        } catch (SQLException ex) {
            throw new ErrorInesperadoException("No se ha podido crear el nuevo registro de cine.");
        }

    }

    //Metodo delegado para obtener la cantidad de cines registrados en el sistema
    public int cantidadCinesRegistrados() throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_CINES);) {

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de cines registrados en el sistema");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de cines");
        }
    }

    //Metodo delegado para obtener el nombre del cine en base a un nombre
    public String verificarNombreDuplicado(String nombre) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_NOMBRE_CINE);) {

            query.setString(1, nombre.trim());

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getString("nombre");
            } else {
                return "";
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener el nombre del cine");
        }
    }

    //Metodo delegado para obtener el nombre del cine en base a un nombre
    public boolean nombrePerteneciente(String nombre, String codigoCine) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_NOMBRE_CINE_END);) {

            query.setString(1, nombre.trim());
            query.setString(2, codigoCine.trim());

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener el nombre del cine");
        }
    }

    //Metodo que permite obtener el listado completo de Cines para el administrador de sistema
    public List<CineDTO> obtenerListadoCinesAsociados(CantidadCargaRequest cargaRequest) throws FormatoInvalidoException, ErrorInesperadoException {

        if (cargaRequest == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        CostoCineDB costoCineDb = new CostoCineDB();

        List<CineDTO> listadoCines = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_CINES);) {

            query.setInt(1, cargaRequest.getLimit());
            query.setInt(2, cargaRequest.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                CineDTO cineEncontrado = new CineDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("estado_anuncios"),
                        resultSet.getBigDecimal("monto_ocultacion").doubleValue(),
                        resultSet.getDate("fecha_creacion").toLocalDate(),
                        resultSet.getString("descripcion"),
                        resultSet.getString("ubicacion")
                );

                listadoCines.add(cineEncontrado);
            }

            for (CineDTO cine : listadoCines) {
                double costoActual = costoCineDb.obtenerCostoActual(cine.getCodigo());
                cine.setCostoCine(costoActual);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los cines asociados al sistema");
        }

        return listadoCines;
    }

    //Metodo que permite obtener un cine en especifico para el administrador de sistema
    public Cine obtenerCine(String idCine) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(idCine)) {
            throw new FormatoInvalidoException("El id del cine esta vacio");
        }

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_CINES_CODIGO);) {

            query.setString(1, idCine);

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                Cine cineEncontrado = new Cine(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("estado_anuncios"),
                        resultSet.getBigDecimal("monto_ocultacion").doubleValue(),
                        resultSet.getDate("fecha_creacion").toLocalDate(),
                        resultSet.getString("descripcion"),
                        resultSet.getString("ubicacion")
                );

                return cineEncontrado;

            } else {
                throw new DatosNoEncontradosException("No se ha encontrado un registro con el codigo de cine enviado");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos del cine asociado al sistema");
        }

    }

    //Metodo que sirve para poder editar los datos de un cine
    public boolean editarCine(Cine cineEditar) throws ErrorInesperadoException, FormatoInvalidoException {

        if (cineEditar == null) {
            throw new FormatoInvalidoException("No se ha enviado ninguna informacion sobre el cine a editar");
        }

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement preparedStmt = conexion.prepareStatement(EDITAR_CINE);) {

            conexion.setAutoCommit(false);

            preparedStmt.setString(1, cineEditar.getNombre());
            preparedStmt.setBigDecimal(2, new BigDecimal(cineEditar.getMontoOcultacion()));
            preparedStmt.setString(3, cineEditar.getDescripcion());
            preparedStmt.setString(4, cineEditar.getUbicacion());
            preparedStmt.setString(5, cineEditar.getCodigo());

            int filasAfectadas = preparedStmt.executeUpdate();

            if (filasAfectadas > 0) {

                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new ErrorInesperadoException("No se ha podido editar el cine.");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback al editar el cine");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden al editar el cine");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                System.out.println("Error al reactivar la autoconfirmacion al editar el cine");
            }
        }
    }

    //==================================== APARTADO DE METODOS QUE PRODUCEN INFORMACION PARA LOS MENUS =========================
    //Metodo que permite obtener el listado de cines llave-valor
    public List<CineAsociadoDTOResponse> obtenerListadoCinesLlaveValor(CantidadCargaRequest cargaRequest) throws FormatoInvalidoException, ErrorInesperadoException {

        if (cargaRequest == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<CineAsociadoDTOResponse> listadoCines = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_CINE_CODIGO_VALOR);) {

            query.setInt(1, cargaRequest.getLimit());
            query.setInt(2, cargaRequest.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                CineAsociadoDTOResponse cineAsociado = new CineAsociadoDTOResponse(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre")
                );

                listadoCines.add(cineAsociado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los cines asociados al sistema");
        }

        return listadoCines;
    }

    //Metodo que permite obtener el listado de cines llave-valor
    public List<CineInformacionDTO> obtenerListadoCinesPrincipal(CantidadCargaRequest cargaRequest) throws FormatoInvalidoException, ErrorInesperadoException {

        if (cargaRequest == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<CineInformacionDTO> listadoCines = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_CINE_DASHBOARD);) {

            query.setInt(1, cargaRequest.getLimit());
            query.setInt(2, cargaRequest.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                CineInformacionDTO cineAsociado = new CineInformacionDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre"),
                        resultSet.getString("descripcion"),
                        resultSet.getString("ubicacion")
                );

                listadoCines.add(cineAsociado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los cines registrados en el sistema para el menu principal");
        }

        return listadoCines;
    }

    //Metodo que permite retornar un cine en especifico
    public CineInformacionDTO obtenerCinesPrincipalCodigo(String codigoCine) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(codigoCine)) {
            throw new FormatoInvalidoException("El id del cine esta vacio");
        }

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_CINE_CODIGO_DASHBOARD);) {

            query.setString(1, codigoCine.trim());

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                CineInformacionDTO cineAsociado = new CineInformacionDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre"),
                        resultSet.getString("descripcion"),
                        resultSet.getString("ubicacion")
                );

                return cineAsociado;

            } else {

                throw new DatosNoEncontradosException("No se ha encontrado informacion sobre el cine solicitado");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los cines registrados en el sistema para el menu principal");
        }

    }

    //==================================== FIN DEL APARTADO DE METODOS QUE PRODUCEN INFORMACION PARA LOS MENUS =========================
    //===================================== APARTADO DE CONSULTAS QUE PERMITEN INTERACTUAR CON LOS CINES ASOCIADOS DE UN ADMINISTRADOR===============
    //Metodo que permite obtener el listado completo de cines asociados a un administrador de cine
    public List<CineDTO> cinesAsociadosAdministradorCine(String idUsuario, CantidadCargaRequest cargaRequest) throws FormatoInvalidoException, ErrorInesperadoException {

        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El id del usuario esta vacio");
        }

        CostoCineDB costoCineDb = new CostoCineDB();

        List<CineDTO> listadoCines = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CINES_ASOCIADOS_CODIGO);) {

            query.setString(1, idUsuario.trim());
            query.setInt(2, cargaRequest.getLimit());
            query.setInt(3, cargaRequest.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                CineDTO cineEncontrado = new CineDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("estado_anuncios"),
                        resultSet.getBigDecimal("monto_ocultacion").doubleValue(),
                        resultSet.getDate("fecha_creacion").toLocalDate(),
                        resultSet.getString("descripcion"),
                        resultSet.getString("ubicacion")
                );

                listadoCines.add(cineEncontrado);
            }

            for (CineDTO cine : listadoCines) {
                double costoActual = costoCineDb.obtenerCostoActual(cine.getCodigo());
                cine.setCostoCine(costoActual);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los cines asociados al sistema");
        }

        return listadoCines;
    }

    //Metodo delegado para obtener la cantidad de cines a los que se encuentra asignado el administrador de cine
    public int cantidadCinesAsignados(String idUsuario) throws ErrorInesperadoException, DatosNoEncontradosException, FormatoInvalidoException {

        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El id del usuario esta vacio");
        }
        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_CINES_ASOCIADOS);) {

            query.setString(1, idUsuario.trim());
            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de cines asignados para el administador");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de cines asignados");
        }
    }

}
