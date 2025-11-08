import { CommonModule, NgClass, NgIf, NgSwitch, NgSwitchCase } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ModalService } from '../modal.service';

@Component({
  selector: 'app-confirm-modal',
  imports: [CommonModule, NgIf, NgSwitch, NgSwitchCase, NgClass],
  templateUrl: './confirm-modal.component.html',
  styleUrl: './confirm-modal.component.scss'
})
export class ConfirmModalComponent {

  mostrar = false;
  mensaje = '';
  tipo: 'exito' | 'error' | 'info' = 'info';

  constructor(private modalService: ModalService) {
    this.modalService.modal$.subscribe(data => {
      if (data) {
        this.mensaje = data.mensaje;
        this.tipo = data.tipo ?? 'info';
        this.mostrar = true;
      } else {
        this.mostrar = false;
      }
    });
  }

  onConfirmar(): void {
    this.modalService.responder(true);
    this.mostrar = false;
  }

  onCancelar(): void {
    this.modalService.responder(false);
    this.mostrar = false;
  }

}
