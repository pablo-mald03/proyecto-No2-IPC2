import { CommonModule, NgIf } from '@angular/common';
import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-shared-popup',
  imports: [NgIf, CommonModule],
  templateUrl: './shared-popup.component.html',
  styleUrl: './shared-popup.component.scss'
})
export class SharedPopupComponent {


  @Input() mensaje: string = '';
  @Input() tipo: 'error' | 'exito' | 'info' = 'info';
  @Input() mostrar: boolean = false;
  @Input() duracion: number = 2500;

  @Output() cerrarEvent = new EventEmitter<void>();

  private timeoutId?: any;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['mostrar']?.currentValue) {
      if (this.timeoutId) clearTimeout(this.timeoutId);
      this.timeoutId = setTimeout(() => this.cerrar(), this.duracion);
    }
  }

  cerrar(): void {
    this.mostrar = false;
    this.cerrarEvent.emit();
  }
}
