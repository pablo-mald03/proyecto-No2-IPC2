import { Component, Input } from '@angular/core';
import { ReporteSalasGustadasDTO } from '../../../models/reportes-cine/reporte-sala-gustada-dto';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-salas-gustadas-cine-cards',
  imports: [DatePipe],
  templateUrl: './salas-gustadas-cine-cards.component.html',
  styleUrl: './salas-gustadas-cine-cards.component.scss'
})
export class SalasGustadasCineCardsComponent {


  @Input() salaGustada!: ReporteSalasGustadasDTO;
}
