import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { CambiarPrecioDTO } from "../../models/anuncios/cambiar-precio-dto";
import { VigenciaAnuncio } from "../../models/anuncios/vigencia-anuncio";

@Injectable({
    providedIn: 'root'
})


export class VigenciaAnunciosService {

    restConstants = new RestConstants();
    constructor(private httpClient: HttpClient) { }


    //Endpoint que retorna las vigencias de los anuncios
    public listadoVigencias(): Observable<VigenciaAnuncio[]> {
        return this.httpClient.get<VigenciaAnuncio[]>(`${this.restConstants.getApiURL()}tarifas/anuncios`);
    }

    //Endpoint que ayuda a obtener una vigencia en especifico
    public vigenciaAnuncioCodigo(codigo: number): Observable<VigenciaAnuncio> {
        return this.httpClient.get<VigenciaAnuncio>(`${this.restConstants.getApiURL()}tarifas/anuncios/${codigo}`);
    }

    //Endpoint que permite cambiar el precio de la vigencia
    public cambiarPrecio(estado: CambiarPrecioDTO): Observable<void> {
        return this.httpClient.put<void>(`${this.restConstants.getApiURL()}tarifas/anuncios`, estado);
    }



}