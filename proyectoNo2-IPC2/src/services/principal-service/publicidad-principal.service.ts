import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { Observable } from "rxjs";
import { AnuncioPublicidadDTO } from "../../models/anuncios/anuncio-publicidad-dto";

@Injectable({
    providedIn: 'root'
})


export class PublicidadPincipalService {

    restConstants = new RestConstants();
    constructor(private httpClient: HttpClient) { }


    //Endpoint que retorna aletoriamente anuncios para genrar publicidad
    public listadoPublicidad(numero: number): Observable<AnuncioPublicidadDTO[]> {
        return this.httpClient.get<AnuncioPublicidadDTO[]>(`${this.restConstants.getApiURL()}principal/publicidad/${numero}`);
    }





}