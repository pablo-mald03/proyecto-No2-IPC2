import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ConfiguracionAnuncioDTO } from "../../models/anuncios/configuracion-anuncio-dto";
import { CambiarPrecioDTO } from "../../models/anuncios/cambiar-precio-dto";

@Injectable({
    providedIn: 'root'
})


export class ConfiguracionAnunciosService {

    restConstants = new RestConstants();
    constructor(private httpClient: HttpClient) { }


    //Endpoint que retorna las configuraciones de los anuncios
    public listadoConfiguraciones(): Observable<ConfiguracionAnuncioDTO[]> {
        return this.httpClient.get<ConfiguracionAnuncioDTO[]>(`${this.restConstants.getApiURL()}configuraciones/anuncios`);
    }

    //Endpoint que ayuda a obtener una configuracion en especifico
    public configuracionAnuncioCodigo(codigo: number): Observable<ConfiguracionAnuncioDTO> {
        return this.httpClient.get<ConfiguracionAnuncioDTO>(`${this.restConstants.getApiURL()}configuraciones/anuncios/${codigo}`);
    }

    //Endpoint que permite cambiar el precio de la configuracion
    public cambiarPrecio(estado: CambiarPrecioDTO): Observable<void> {
        return this.httpClient.put<void>(`${this.restConstants.getApiURL()}configuraciones/anuncios`, estado);
    }



}