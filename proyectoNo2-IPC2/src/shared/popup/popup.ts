import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export interface PopupData {
  mensaje: string;
  tipo: 'error' | 'exito' | 'info';
  duracion?: number;
}

@Injectable()

export class Popup {

  private popupSubject = new Subject<PopupData>();
  popup$ = this.popupSubject.asObservable();

  //Metodo que permite mostrar el popup
  mostrarPopup(data: PopupData) {
    this.popupSubject.next(data);
  }

}
