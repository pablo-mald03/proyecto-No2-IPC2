import { CommonModule, NgIf, NgSwitch, NgSwitchCase } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-fullscreen-modal',
  imports: [NgIf,NgSwitchCase, NgSwitch, CommonModule],
  templateUrl: './fullscreen-modal.component.html',
  styleUrl: './fullscreen-modal.component.scss'
})
export class FullscreenModalComponent implements OnChanges {


  @Input() mensaje: string = '';
  @Input() tipo: 'exito' | 'error' | 'info' = 'info';
  @Input() mostrar: boolean = false;
  @Input() duracion: number = 2000;

  @Output() cerrarEvent = new EventEmitter<void>();

  private timeoutId?: any;

  //Metodo que detecta los cambios en el modal
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['mostrar']?.currentValue) {
      if (this.timeoutId) clearTimeout(this.timeoutId);
      this.timeoutId = setTimeout(() => this.cerrar(), this.duracion);
    }
  }

  //Metodo que sirve para poder cerrar el modal
  cerrar(): void {
    this.mostrar = false;
    this.cerrarEvent.emit();
  }

}
