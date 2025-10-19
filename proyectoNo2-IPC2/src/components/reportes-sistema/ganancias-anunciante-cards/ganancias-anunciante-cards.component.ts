import { CommonModule, DatePipe, NgClass } from '@angular/common';
import { Component, Input, Pipe } from '@angular/core';
import { ReporteAnuncianteDTO } from '../../../models/reportes/anunciante-dto';

@Component({
  selector: 'app-ganancias-anunciante-cards',
  imports: [DatePipe, NgClass, CommonModule],
  templateUrl: './ganancias-anunciante-cards.component.html',
  styleUrl: './ganancias-anunciante-cards.component.scss'
})
export class GananciasAnuncianteCardsComponent {


  @Input() anunciante!: ReporteAnuncianteDTO;
}
