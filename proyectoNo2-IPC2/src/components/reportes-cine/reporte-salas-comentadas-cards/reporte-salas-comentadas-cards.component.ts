import { Component, Input } from '@angular/core';
import { ReporteSalasComentadasDTO } from '../../../models/reportes-cine/reporte-salas-comentarios-dto';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-reporte-salas-comentadas-cards',
  imports: [DatePipe],
  templateUrl: './reporte-salas-comentadas-cards.component.html',
  styleUrl: './reporte-salas-comentadas-cards.component.scss'
})
export class ReporteSalasComentadasCardsComponent {

  @Input() salaComentarios!: ReporteSalasComentadasDTO;
}
