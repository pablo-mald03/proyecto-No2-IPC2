import { CommonModule, NgFor } from '@angular/common';
import { Component, Input } from '@angular/core';
import { ReporteBoletosVendidosDTO } from '../../../models/reportes-cine/reporte-boletos-vendidos-dto';

@Component({
  selector: 'app-boletos-vendidos-cards',
  imports: [CommonModule],
  templateUrl: './boletos-vendidos-cards.component.html',
  styleUrl: './boletos-vendidos-cards.component.scss'
})
export class BoletosVendidosCardsComponent {

@Input() reporteBoletos!: ReporteBoletosVendidosDTO;

}
