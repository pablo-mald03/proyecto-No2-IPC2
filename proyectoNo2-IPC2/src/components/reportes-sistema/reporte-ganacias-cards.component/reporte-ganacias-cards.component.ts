import { Component, Input } from '@angular/core';
import { GananciasSistemaDTO } from '../../../models/reportes/ganancias-sistema-dto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reporte-ganacias-cards',
  imports: [CommonModule],
  templateUrl: './reporte-ganacias-cards.component.html',
  styleUrl: './reporte-ganacias-cards.component.scss'
})
export class ReporteGanaciasCardsComponent {

  @Input() gananciaProcesada!: GananciasSistemaDTO;
}
