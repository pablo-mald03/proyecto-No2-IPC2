import { NgIf } from '@angular/common';
import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-shared-popup',
  imports: [NgIf],
  templateUrl: './shared-popup.component.html',
  styleUrl: './shared-popup.component.scss'
})
export class SharedPopupComponent {

  @Input() mensaje: string = '';
  @Input() tipo: 'error' | 'exito' | 'info' = 'info';
  @Input() mostrar: boolean = false;
  @Input() duracion: number = 4000;

  @Output() cerrarEvent = new EventEmitter<void>();

  //Metodo que detecta los cambios en el componente pop up 
  private timeoutId: any;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['mostrar'] && this.mostrar) {
      // Limpia timeout previo si existe
      if (this.timeoutId) clearTimeout(this.timeoutId);

      this.timeoutId = setTimeout(() => this.cerrar(), this.duracion);
    }
  }

  //Metodo que sirve para cerrar el pop up
  cerrar(): void {
    this.mostrar = false;
    this.cerrarEvent.emit();
  }
}
