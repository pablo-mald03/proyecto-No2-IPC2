import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { FotoPerfilDTO } from "../../models/usuarios/foto-perfil-dto";


@Injectable({
    providedIn: 'root'
})

export class FotoPerfilSercive {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint para recibir la foto de perfil base64
    public obtenerFoto(id: string): Observable<FotoPerfilDTO> {
        return this.httpClient.get<FotoPerfilDTO>(`${this.restConstants.getApiURL()}usuarios/foto/${id}`);
    }
}