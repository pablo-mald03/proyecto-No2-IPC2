import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';


interface ModalData {
  mensaje: string;
  tipo?: 'exito' | 'error' | 'info';
  resolver?: (valor: boolean) => void;
}

@Injectable({ providedIn: 'root' })
export class ModalService {
  private modalSubject = new BehaviorSubject<ModalData | null>(null);
  modal$ = this.modalSubject.asObservable();

  confirmar(mensaje: string, tipo?: 'exito' | 'error' | 'info'): Promise<boolean> {
    return new Promise(resolve => {
      this.modalSubject.next({ mensaje, tipo, resolver: resolve });
    });
  }

  responder(valor: boolean) {
    const data = this.modalSubject.value;
    if (data?.resolver) data.resolver(valor);
    this.modalSubject.next(null); 
  }
}
