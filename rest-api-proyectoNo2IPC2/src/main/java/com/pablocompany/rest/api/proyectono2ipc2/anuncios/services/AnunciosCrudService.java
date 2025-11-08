/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.services;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.database.AnunciosDB;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.database.ConfiguracionAnuncioDB;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.database.VigenciaAnuncioDB;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.VigenciaAnuncio;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.Anuncio;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.AnuncioDTORequest;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.ConfiguracionAnuncioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.billetera.database.BilleteraDigitalDB;
import com.pablocompany.rest.api.proyectono2ipc2.billetera.models.SaldoBilleteraDTO;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.TransaccionInvalidaException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase que permite gestionar todoas las interacciones DEL ANUNCIANTE 
public class AnunciosCrudService {

    //Metodo que permite al usuario comprar un nuevo anuncio
    public boolean comprarAnuncio(AnuncioDTORequest request) throws FormatoInvalidoException, DatosNoEncontradosException, ErrorInesperadoException, TransaccionInvalidaException {

        AnunciosDB anuncioDb = new AnunciosDB();

        VigenciaAnuncioDB vigenciaAnuncioDb = new VigenciaAnuncioDB();

        ConfiguracionAnuncioDB configuracionAnuncioDb = new ConfiguracionAnuncioDB();

        double montoPago = extraerMonto(request);

        if (validarExistenciaTipos(request, vigenciaAnuncioDb, configuracionAnuncioDb)) {

            Anuncio nuevoAnuncio = extraerDatosRequest(request, anuncioDb, vigenciaAnuncioDb, configuracionAnuncioDb, montoPago);
            
            if(nuevoAnuncio.validarDatos()){
                
                BilleteraDigitalDB billeteraDigitalDb = new BilleteraDigitalDB();
                
                SaldoBilleteraDTO saldoCliente = billeteraDigitalDb.obtenerSaldoActual(nuevoAnuncio.getIdUsuario());
                
                if(montoPago > saldoCliente.getSaldo()){
                    throw new TransaccionInvalidaException("No cuentas con el saldo suficiente para comprar el anuncio");
                }
                
               return anuncioDb.comprarAnuncio(nuevoAnuncio, montoPago);
                
                
            }

        }
        
        throw new ErrorInesperadoException("No se ha podido generar la transaccion");

    }

    //Metodo delegado para evaluar y terminar de formatear el request para configuararlo acorde a su tipo
    private Anuncio formatearAnuncio(ConfiguracionAnuncioDTO configuracion, Anuncio nuevoAnuncio, double montoPago, VigenciaAnuncio vigencia) throws FormatoInvalidoException {

        if ((configuracion.getCodigo() == 1 && nuevoAnuncio.getCodigoTipo() == 1)
                || (configuracion.getCodigo() == 2 && nuevoAnuncio.getCodigoTipo() == 2)) {
            nuevoAnuncio.setUrl("null");
        }

        double total = vigencia.getPrecio() + configuracion.getPrecio();

        if (montoPago != total) {
            throw new FormatoInvalidoException("El monto a debitar ha sido manipulado");
        }
        
        

        return nuevoAnuncio;

    }

    //Metodo delegado para poder estraer los datos del request 
    private Anuncio extraerDatosRequest(AnuncioDTORequest request, AnunciosDB anunciosDb, VigenciaAnuncioDB vigenciaAnuncioDb, ConfiguracionAnuncioDB configuracionAnuncioDb, double pago) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(request.getFechaCompra())) {
            throw new FormatoInvalidoException("La fecha de compra esta vacia");
        }

        try {

            int cantidadRegistros = anunciosDb.cantidadAnunciosSistema();

            String codigoAnuncio = generarCodigoAnuncio(cantidadRegistros);

            VigenciaAnuncio vigencia = vigenciaAnuncioDb.obtenerVigenciaCodigo(Integer.parseInt(request.getTipoTarifa()));

            ConfiguracionAnuncioDTO configuracion = configuracionAnuncioDb.obtenerConfiguracionCodigo(Integer.parseInt(request.getCodigoTipo()));

            LocalDate fechaCompraParsed = LocalDate.parse(request.getFechaCompra());

            LocalDate fechaExpiracion = calcularFechaExpiracion(fechaCompraParsed, vigencia.getDuracion());

            ConvertirImagenAnuncioService convertirImagenService = new ConvertirImagenAnuncioService();

            byte[] fotoAnuncio = convertirImagenService.convertirFormatoImagen(request.getFoto());

            Anuncio nuevoAnuncio = new Anuncio(
                    codigoAnuncio,
                    true,
                    request.getNombre(),
                    false,
                    fechaExpiracion,
                    fechaCompraParsed,
                    request.getUrl(),
                    request.getTexto(),
                    fotoAnuncio,
                    configuracion.getCodigo(),
                    request.getIdUsuario());

            return formatearAnuncio(configuracion, nuevoAnuncio, pago, vigencia);

        } catch (NumberFormatException ex) {
            throw new FormatoInvalidoException("Los tipos de anuncio no son numericos");

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("La fecha de compra no tiene formato ISO (yyyy-MM-dd).");
        } catch (IOException ex) {
            throw new FormatoInvalidoException("No se ha podido procesar la imagen del anuncio");
        }
    }

    //Metodo que permite calcular la fecha de expiracion
    private LocalDate calcularFechaExpiracion(LocalDate fechaCompra, double duracionDias) {
        long diasEnteros = (long) duracionDias;
        return fechaCompra.plusDays(diasEnteros);
    }

    //Metodo que ayuda a auto nombrar el codigo de anuncio
    private String generarCodigoAnuncio(int cantidadActual) {
        int siguiente = cantidadActual + 1;
        String numeroFormateado = String.format("%03d", siguiente);
        return "A-" + numeroFormateado;
    }

    //Metodo delegado para poder retornar validar el codigo de existencia
    private boolean validarExistenciaTipos(AnuncioDTORequest request, VigenciaAnuncioDB vigenciaAnuncioDb, ConfiguracionAnuncioDB configuracionAnuncioDb) throws FormatoInvalidoException, ErrorInesperadoException {

        if (StringUtils.isBlank(request.getCodigoTipo())) {
            throw new FormatoInvalidoException("El codigo de tipo de anuncio es vacio");
        }

        if (StringUtils.isBlank(request.getTipoTarifa())) {
            throw new FormatoInvalidoException("El codigo de tipo de tarifa de anuncio esta vacio");
        }

        boolean codigoExistente = false;

        boolean tarifaExistente = false;

        try {

            try {
                int codigoTipo = Integer.parseInt(request.getCodigoTipo());

                vigenciaAnuncioDb.obtenerPrecioCodigo(codigoTipo);

                codigoExistente = true;

            } catch (DatosNoEncontradosException ex) {
                codigoExistente = false;
            }

            try {

                int codigoTarifa = Integer.parseInt(request.getTipoTarifa());

                configuracionAnuncioDb.obtenerPrecioCOdigo(codigoTarifa);

                tarifaExistente = true;

            } catch (DatosNoEncontradosException ex) {

                tarifaExistente = false;
            }

        } catch (NumberFormatException e) {

            throw new FormatoInvalidoException("El codigo de tipo o de tarifa no es numerico");
        }

        return codigoExistente && tarifaExistente;

    }
    //Metodo utilizado para extraer el monto de debitacion

    private double extraerMonto(AnuncioDTORequest request) throws FormatoInvalidoException {

        if (StringUtils.isBlank(request.getMonto())) {
            throw new FormatoInvalidoException("La cantidad de cobro viene vacia");

        }

        try {

            return Double.parseDouble(request.getMonto());

        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("La cantidad de cobro no es numerica");
        }

    }

}
