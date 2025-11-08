import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { AnuncioRegistradoDTO } from "../../models/anuncios/anuncio-registrado-dto";
import { CantidadRegistrosDTO } from "../../models/usuarios/cantidad-registros-dto";

@Injectable({
    providedIn: 'root'
})


export class AnunciosRegistradosService {

    restConstants = new RestConstants();
    constructor(private httpClient: HttpClient) { }


    //Endpoint que retorna dinamicamente los anuncios comprados en el sistema
    public listadoRegistros(limite: number, inicio: number): Observable<AnuncioRegistradoDTO[]> {
        return this.httpClient.get<AnuncioRegistradoDTO[]>(`${this.restConstants.getApiURL()}anuncios/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad total de registros de canuncios comprados en el sistema
    public cantidadRegistros(): Observable<CantidadRegistrosDTO> {
        return this.httpClient.get<CantidadRegistrosDTO>(`${this.restConstants.getApiURL()}anuncios/cantidad`);
    }


}