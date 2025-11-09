import { HttpClient } from "@angular/common/http";
import { Pelicula } from "../../models/peliculas/pelicula";
import { RestConstants } from "../../shared/rest-constants";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root'
})

export class PeliculaSistemaService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Metodo que le permite al usuario crear peliculas
    public crearPelicula(datos: FormData): Observable<void> {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}peliculas`, datos);
    }

    //Endpoint que retorna la cantidad de peliculas registradas
    public listadoRegistros(): Observable<Pelicula[]> {
        return this.httpClient.get<Pelicula[]>(`${this.restConstants.getApiURL()}peliculas/todo`);
    }



}