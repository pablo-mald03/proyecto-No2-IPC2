import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { AnuncioRegistradoDTO } from "../../models/anuncios/anuncio-registrado-dto";
import { CantidadRegistrosDTO } from "../../models/usuarios/cantidad-registros-dto";
import { CambiarEstadoDTO } from "../../models/anuncios/cambiar-estado-dto";
import { CambiarEstadoClienteDTO } from "../../models/anuncios/cambiar-estado-cliente-dto";

@Injectable({
    providedIn: 'root'
})


export class AnunciosRegistradosClienteService {

    restConstants = new RestConstants();
    constructor(private httpClient: HttpClient) { }


    //Endpoint que retorna dinamicamente los anuncios comprados por el anunciante
    public listadoRegistros(limite: number, inicio: number, idUsuario: string): Observable<AnuncioRegistradoDTO[]> {
        return this.httpClient.get<AnuncioRegistradoDTO[]>(`${this.restConstants.getApiURL()}anunciante/anuncios/limit/${limite}/offset/${inicio}/usuario/${idUsuario}`);
    }

    //Endpoint que ayuda a obtener la cantidad total de registros de canuncios comprados por el anunciante
    public cantidadRegistros(idUsuario: string): Observable<CantidadRegistrosDTO> {
        return this.httpClient.get<CantidadRegistrosDTO>(`${this.restConstants.getApiURL()}anunciante/anuncios/cantidad/usuario/${idUsuario}`);
    }

    //Endpoint que permite cambiar el estado de los anuncios por el anunciante
    public cambiarEstado(estado: CambiarEstadoClienteDTO): Observable<void> {
        return this.httpClient.put<void>(`${this.restConstants.getApiURL()}anunciante/anuncios`, estado);
    }



}