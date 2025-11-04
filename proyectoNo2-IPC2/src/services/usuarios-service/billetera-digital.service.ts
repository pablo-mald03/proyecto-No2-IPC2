import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { BilleteraDigital } from "../../models/usuarios/billetera-digital";
import { SaldoBilleteraDTO } from "../../models/usuarios/saldo-billetera-dto";

@Injectable({
    providedIn: 'root'
})

export class BilleteraDigitalService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint para recibir el saldo de la cuenta
    public obtenerSaldo(id: string): Observable<SaldoBilleteraDTO> {
        return this.httpClient.get<SaldoBilleteraDTO>(`${this.restConstants.getApiURL()}billetera/${id}`);
    }

}