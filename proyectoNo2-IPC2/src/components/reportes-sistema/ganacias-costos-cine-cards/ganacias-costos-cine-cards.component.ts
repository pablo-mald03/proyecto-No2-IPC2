import { Component, Input } from '@angular/core';
import { CineCostoDTO } from '../../../models/reportes/cine-costo-dto';
import { CommonModule, DatePipe } from '@angular/common';

@Component({
  selector: 'app-ganacias-costos-cine-cards',
  imports: [DatePipe, CommonModule],
  templateUrl: './ganacias-costos-cine-cards.component.html',
  styleUrl: './ganacias-costos-cine-cards.component.scss'
})
export class GanaciasCostosCineCardsComponent {

  @Input() costoCine!: CineCostoDTO;

}
