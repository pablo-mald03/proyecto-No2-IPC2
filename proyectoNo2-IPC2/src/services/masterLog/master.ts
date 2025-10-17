import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Subject } from "rxjs";

@Injectable({


    providedIn: 'root'
})

export class Master {


    onLogin: Subject<boolean> = new Subject<boolean>();

    constructor(private http: HttpClient) {

    }


}