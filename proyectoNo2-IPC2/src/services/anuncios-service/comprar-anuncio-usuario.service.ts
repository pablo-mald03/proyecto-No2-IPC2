import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})

export class ComprarAnuncioUsuarioService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Metodo que le permite al usuario comprar un nuevo anuncio
    public comprarAnuncio(anuncio: FormData): Observable<void> {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}anuncios`, anuncio);
    }

   

}